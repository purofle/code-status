package tech.archlinux.codestatus.service

import com.apollographql.apollo3.ApolloClient
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import tech.archlinux.codestatus.graphql.GetCommitByRepoQuery
import tech.archlinux.codestatus.graphql.GetContributedReposQuery
import tech.archlinux.codestatus.pojo.UserLogin

@Service
class GithubAPIService {

    @Autowired
    private lateinit var reactive: ReactiveStringRedisTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Resource
    lateinit var redisTemplate: RedisTemplate<String, UserLogin>

    val log: Logger = LoggerFactory.getLogger(GithubAPIService::class.java)
    val webClient = WebClient.create("https://api.github.com/")

    /**
     * 从 token 获取用户名, 默认使用缓存
     * @param accessToken token
     */
    suspend fun getUserName(accessToken: String): UserLogin {

        val cacheUserLogin = redisTemplate.opsForValue().get(accessToken)

        cacheUserLogin?.let {
            return it
        }

        val userLogin = webClient.get()
                .uri("user")
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
            .bodyToFlow<UserLogin>()
            .catch { e ->
                log.error("Failed to get user name", e)
                throw RuntimeException("Failed to get user name")
            }
            .first()

        redisTemplate.opsForValue().set(accessToken, userLogin)

        return userLogin
    }

    suspend fun recentlyCommit(accessToken: String): List<GetCommitByRepoQuery.Data> = coroutineScope {

        val apolloClient = ApolloClient.Builder()
            .addHttpHeader("Authorization", "Bearer $accessToken")
            .serverUrl("https://api.github.com/graphql")
            .build()

        val username = getUserName(accessToken).login

        val response = apolloClient.query(GetContributedReposQuery(username)).execute()

        val data = response.data
        val contributedRepos = data!!.user!!.repositoriesContributedTo.nodes
            ?.filterNotNull()
            ?.filterNot { it.isPrivate }

        val commitsResponse = contributedRepos?.map {
            async(Dispatchers.IO) {
                log.debug("Repo: {}", it)
                val commits = apolloClient.query(GetCommitByRepoQuery(it.name, it.owner.login)).execute()
                if (commits.hasErrors()) {
                    log.error("Failed to get commits: {}", commits.errors)
                }
                commits.data!!
            }
        } ?: emptyList()

        return@coroutineScope commitsResponse.awaitAll()
    }

}

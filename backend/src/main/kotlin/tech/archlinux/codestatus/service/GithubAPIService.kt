package tech.archlinux.codestatus.service

import com.apollographql.apollo3.ApolloClient
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
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


    fun restTemplate(accessToken: String): RestTemplate = RestTemplateBuilder(RestTemplateCustomizer { rt: RestTemplate ->
        rt.interceptors.add(
            ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                request.headers.apply {
                    add("Authorization", "Bearer $accessToken")
                    add("X-GitHub-Api-Version", "2022-11-28")
                    add("content-type", "application/json")
                    add("Accept", "application/vnd.github.v3+json")
                }
                execution.execute(request, body!!)
            })
    }).build()

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

    fun graphqlRequest(accessToken: String, requestBody: String): String? {
        val request = HttpEntity(requestBody)
        return restTemplate(accessToken).postForObject("https://api.github.com/graphql", request, String::class.java)
    }

    suspend fun recentlyCommit(accessToken: String) {

        val webClient = WebClient.builder()

        val apolloClient = ApolloClient.Builder()
            .addHttpHeader("Authorization", "Bearer $accessToken")
            .serverUrl("https://api.github.com/graphql")
            .build()

        val username = getUserName(accessToken).login

        val response = apolloClient.query(GetContributedReposQuery(username)).execute()

        if (response.hasErrors()) {
            log.error("Failed to get contributed repos, errors: {}", response.errors?.toString())
        }

        val data = response.data
        data?.user?.repositoriesContributedTo?.nodes?.filterNotNull()?.forEach {
            log.debug("repo: {}", it)
        }
    }

}

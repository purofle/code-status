package tech.archlinux.codestatus.service

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.pojo.UserLogin
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class GithubAPIService {

    @Autowired
    private lateinit var reactive: ReactiveStringRedisTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

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
    @Cacheable("client", key = "#accessToken")
    suspend fun getUserName(accessToken: String): UserLogin {

        return webClient.get()
                .uri("user")
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
            .bodyToFlow<UserLogin>()
            .catch { e ->
                log.error("Failed to get user name", e)
                throw RuntimeException("Failed to get user name")
            }
            .first()
    }

    fun graphqlRequest(accessToken: String, requestBody: String): String? {
        val request = HttpEntity(requestBody)
        return restTemplate(accessToken).postForObject("https://api.github.com/graphql", request, String::class.java)
    }

    fun recentlyCommit(accessToken: String): HashMap<Repository, List<Commit>> {

        val webClient = WebClient.builder()
        HttpGraphQlClient.builder(webClient)
            .headers { it.setBearerAuth(accessToken) }
            .build()

        // 获取上周的日期，格式为 2023-03-04T00:00:00
        val time = OffsetDateTime.now(ZoneId.of("UTC")).minusWeeks(1).format(DateTimeFormatter.ISO_DATE_TIME)

        // 读取 resources/graphql/getCommit.graphql
        val context = ClassPathResource("graphql/getCommit.graphql").inputStream.reader().readText()
            .format(time)



        val jsonObject = objectMapper.createObjectNode()
            .apply { put("query", context) }

        val rawResult = graphqlRequest(accessToken, jsonObject.toString())

        if (rawResult == null) {
            log.error("Failed to get commit data")
            throw RuntimeException("Failed to get commit data")
        }

        log.debug("Commit: $rawResult")

        val commits = HashMap<Repository, List<Commit>>()

        // ["data"]["search"]["nodes"]
        objectMapper.readTree(rawResult)
            .findValue("data")
            .findValue("search")
            .findValue("nodes").filterNot {
                /**
                 * 过滤掉没有 commit 的项目
                 * python:
                 * filter(lambda x: len(x) == 0, ["data"]["search"]["nodes"])
                 */
                log.debug("${it.findValue("defaultBranchRef").findValue("target").findValue("history").findValue("nodes").size() == 0}")
                log.debug(it.findValue("defaultBranchRef").findValue("target").findValue("history").findValue("nodes").toPrettyString())
                it.findValue("defaultBranchRef")
                .findValue("target")
                .findValue("history")
                .findValue("nodes").size() == 0
        }.forEach {
                // such as ["data"]["search"]["nodes"][0]
                val repo = Repository(
                    id = 0, // id用不上
                    nodeId = it.findValue("id").asText(),
                    fullName = it.findValue("nameWithOwner").asText(),
                    isPrivate = it.findValue("isPrivate").asBoolean(),
                )

                val commit = Commit(
                    id = it.findValue("oid").asText(),
                    treeId = it.findValue("id").asText(),
                    timestamp = OffsetDateTime.parse(it.findValue("committedDate").asText()),
                    url = it.findValue("commitUrl").asText(),
                    message = it.findValue("message").asText(),
                    added = it.findValue("additions").asInt(),
                    removed = it.findValue("deletions").asInt(),
                    modified = it.findValue("changedFilesIfAvailable").asInt(),
                )

                // 放入 hashMap, 一个 repo 对应一个 List<Commit>, list 内有多个 commit
                commits.getOrPut(repo) { mutableListOf(commit) }.let { list ->
                    log.debug("getOrPut: ${list.size} $list")
                    commits[repo] = list.plus(commit)
                }

            }

        return commits
    }

}

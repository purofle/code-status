package tech.archlinux.codestatus.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class GithubAPIService {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    val log: Logger = LoggerFactory.getLogger(GithubAPIService::class.java)

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
    fun getUserName(accessToken: String): String {

        val userRaw = restTemplate(accessToken).getForObject("https://api.github.com/user", Map::class.java)

        userRaw?.map {
            it.key.toString() to it.value.toString()
        }?.toMap()?.let {
            return it["login"] ?: throw RuntimeException("User not found")
        } ?: throw RuntimeException("User not found")
    }

    fun graphqlRequest(accessToken: String, requestBody: String): String? {
        val request = HttpEntity(requestBody)
        return restTemplate(accessToken).postForObject("https://api.github.com/graphql", request, String::class.java)
    }

    fun recentlyCommit(accessToken: String): HashMap<Repository, Commit> {

        // 获取上周的日期，格式为 2023-03-04T00:00:00
        val time = OffsetDateTime.now(ZoneId.of("UTC")).minusWeeks(1).format(DateTimeFormatter.ISO_DATE_TIME)

        // 读取 resources/graphql/getCommit.graphql
        val context = ClassPathResource("graphql/getCommit.graphql").inputStream.reader().readText()
            .format(time)

        log.debug("Context: $context")

        val jsonObject = objectMapper.createObjectNode()
            .apply { put("query", context) }

        val rawResult = graphqlRequest(accessToken, jsonObject.toString())

        if (rawResult == null) {
            log.error("Failed to get commit data")
            throw RuntimeException("Failed to get commit data")
        }

        log.debug("Commit: $rawResult")

        val commits = HashMap<Repository, Commit>()

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
                /**
                 * 过滤掉没有 commit 的项目
                 * python:
                 * filter(lambda x: len(x) == 0, ["data"]["search"]["nodes"])
                 */
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
                    added = List(it.findValue("additions").asInt()) { "" },
                    removed = List(it.findValue("deletions").asInt()) { "" },
                    modified = List(it.findValue("changedFilesIfAvailable").asInt()) { "" },
                )

                commits[repo] = commit
            }

        return commits
    }

}

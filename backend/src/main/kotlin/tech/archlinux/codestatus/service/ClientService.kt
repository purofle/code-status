package tech.archlinux.codestatus.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@CacheConfig(cacheNames = ["client"])
class ClientService {

    /**
     * 从 token 获取用户名, 默认使用缓存
     * @param accessToken token
     */
    @Cacheable("client", key = "#accessToken")
        fun getUserName(accessToken: String): String {

        val restTemplate = RestTemplateBuilder(RestTemplateCustomizer { rt: RestTemplate ->
            rt.interceptors.add(
                ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                    request.headers.apply {
                        add("Authorization", "Bearer $accessToken")
                        add("X-GitHub-Api-Version", "2022-11-28")
                        add("Accept", "application/vnd.github.v3+json")
                    }
                    execution.execute(request, body!!)
                })
        }).build()

        val userRaw = restTemplate.getForObject("https://api.github.com/user", Map::class.java)

        userRaw?.map {
            it.key.toString() to it.value.toString()
        }?.toMap()?.let {
            return it["login"] ?: throw RuntimeException("User not found")
        } ?: throw RuntimeException("User not found")
    }
}

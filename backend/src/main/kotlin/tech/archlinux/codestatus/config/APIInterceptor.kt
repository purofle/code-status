package tech.archlinux.codestatus.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import tech.archlinux.codestatus.service.GithubAPIService

@Component
class APIInterceptor: HandlerInterceptor {

    @Autowired
    lateinit var githubAPIService: GithubAPIService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.headerNames.toList().firstOrNull { it.lowercase() == "authorization" } == null) {
            throw RuntimeException("Authorization not found")
        }

        // 判断是否是 Github Token
        val token = request.getHeader("Authorization")
        if (token.length != 40) {
            throw RuntimeException("Authorization not match")
        }

        return true
    }
}

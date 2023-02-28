package tech.archlinux.codestatus.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class HttpServletFilter : Filter {
    val log: Logger = LoggerFactory.getLogger(HttpServletFilter::class.java)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        when (request) {
            is HttpServletRequest -> {
                log.info("Request: ${request.method} ${request.requestURI}")
                val requestWrapper = RequestWrapper(request)
                chain.doFilter(requestWrapper, response)
            }

            else -> {
                chain.doFilter(request, response)
            }
        }
    }
}

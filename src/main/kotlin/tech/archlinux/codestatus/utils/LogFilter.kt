package tech.archlinux.codestatus.utils

import jakarta.servlet.FilterChain
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.core.util.IOUtils
import org.springframework.web.filter.OncePerRequestFilter

//@WebFilter(filterName = "logFilter", urlPatterns = ["/*"])
class LogFilter: OncePerRequestFilter() {
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		logger.info("(${request.method}): ${IOUtils.toString(request.reader)}")
	}
}

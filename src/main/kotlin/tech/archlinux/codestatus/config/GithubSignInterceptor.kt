package tech.archlinux.codestatus.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import tech.archlinux.codestatus.utils.GithubUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

@Component
class GithubSignInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var githubUtils: GithubUtils
    val log: Log = LogFactory.getLog(this::class.java)

    override fun preHandle(request: HttpServletRequest, res: HttpServletResponse, handler: Any): Boolean {

        val requestWrapper = RequestWrapper(request)

        if (githubUtils.debug) {
            return true
        }

        // 判断是否存在 X-Hub-Signature-256
        request.headerNames.toList().firstOrNull { it == "x-hub-signature-256" }
            ?: throw RuntimeException("X-Hub-Signature-256 not found")

        // 获取 body bytes
        val body = getRequestString(requestWrapper.inputStream) ?: throw RuntimeException("body not found")

        request.setAttribute("requestBody", body)

        // 获取签名
        val signPass = githubUtils.validateSignature(request.getHeader("x-hub-signature-256"), body)

        log.debug("body: $body")

        if (!signPass) {
            throw RuntimeException("X-Hub-Signature-256 not match")
        }

        return true
    }

    private fun getRequestString(inputStream: InputStream): String? {
        return try {
            val streamReader = BufferedReader(InputStreamReader(inputStream, Charset.defaultCharset()))
            val sb = StringBuilder()
            var inputStr: String?
            while (streamReader.readLine().also { inputStr = it } != null) {
                sb.append(inputStr)
            }
            sb.toString()
        } catch (e: Exception) {
            null
        }
    }

}

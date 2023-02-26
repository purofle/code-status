package tech.archlinux.codestatus.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import tech.archlinux.codestatus.utils.GithubUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset


// FIXME: 2023/02/26 签名验证有误
@Component
class GithubSignFilter : HandlerInterceptor {

    @Autowired
    lateinit var githubUtils: GithubUtils
    val log: Log = LogFactory.getLog(this::class.java)

    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any): Boolean {

        val request = ContentCachingRequestWrapper(req)

        // 判断是否存在 X-Hub-Signature-256
        val isSigned = request.headerNames.toList().firstOrNull { it == "x-hub-signature-256" }

        if (isSigned == null) {
            returnError(res, "X-Hub-Signature-256 not found")
            return false
        }

        // 获取 body bytes
        val body = getRequestString(request)?.toByteArray() ?: return false
        // 获取签名
        val signPass = githubUtils.validateSignature(request.getHeader("x-hub-signature-256"), body)

        log.debug(body)
        if (signPass) {
            return true
        }

        returnError(res, "X-Hub-Signature-256 not match")
        return false
    }

    private fun getRequestString(request: HttpServletRequest): String? {
        return try {
            val streamReader = BufferedReader(InputStreamReader(request.inputStream, Charset.defaultCharset()))
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

    fun returnError(res: HttpServletResponse, msg: String) {
        res.status = 403
        res.writer.also {
            it.println(msg)
            it.close()
        }
    }

}

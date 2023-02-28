package tech.archlinux.codestatus.controller

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.ContentCachingRequestWrapper
import tech.archlinux.codestatus.WebHookType
import tech.archlinux.codestatus.config.AppConfig
import tech.archlinux.codestatus.service.GithubWebhookService
import java.util.*

@RestController
class GithubWebhookController {

    @Autowired
    lateinit var githubWebhookService: GithubWebhookService
    private val log: Logger = LoggerFactory.getLogger(GithubWebhookController::class.java)

    @RequestMapping(AppConfig.webhook, method = [RequestMethod.POST])
    fun handlerWebhook(
        req: HttpServletRequest,
        @RequestBody(required = false) requestBody: Map<String, Any>,
    ) {

        val request = ContentCachingRequestWrapper(req)

        // 查看所有 header
        request.headerNames.toList().forEach {
            log.debug("header: $it -> ${request.getHeader(it)}")
        }

        val login = ((requestBody["sender"]) as Map<*, *>)["login"] as String

        log.info("sender: $login")

        githubWebhookService.handleWebhook(
            WebHookType.valueOf(request.getHeader("x-github-event").uppercase(Locale.getDefault())),
            request.reader.readText()
        )
    }
}

package tech.archlinux.codestatus.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
import tech.archlinux.codestatus.entity.Account
import tech.archlinux.codestatus.entity.Sender
import tech.archlinux.codestatus.repository.AccountRepository
import tech.archlinux.codestatus.service.GithubWebhookService
import java.util.*

@RestController
class GithubWebhookController {

    @Autowired
    lateinit var githubWebhookService: GithubWebhookService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

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

        val sender = objectMapper.convertValue(requestBody["sender"], Sender::class.java)
        val pusher = requestBody["pusher"] as Map<*, *>
        val email = pusher["email"] as String

        sender.apply {
            // 判断用户是否存在
            if (!accountRepository.existsAccountById(id)) {
                accountRepository.save(
                    Account(
                        id = id,
                        name = login,
                        nodeId = nodeId,
                        avatarUrl = avatarUrl,
                        email = email
                    )
                )
                log.debug("new account: $login ($id)")
            }
        }

        githubWebhookService.handleWebhook(
            WebHookType.valueOf(request.getHeader("x-github-event").uppercase(Locale.getDefault())),
            request.reader.readText()
        )
    }
}

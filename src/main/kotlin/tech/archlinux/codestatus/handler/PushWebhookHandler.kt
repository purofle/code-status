package tech.archlinux.codestatus.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tech.archlinux.codestatus.WebHookType

@Component
class PushWebhookHandler : GithubWebhookHandler {
    override val webhookType: WebHookType
        get() = WebHookType.PUSH

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(payload: String) {
        logger.info("Handling push webhook")

    }
}

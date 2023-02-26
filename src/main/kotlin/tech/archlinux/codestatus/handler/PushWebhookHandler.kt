package tech.archlinux.codestatus.handler

import org.springframework.stereotype.Component
import tech.archlinux.codestatus.WebHookType

@Component
class PushWebhookHandler : GithubWebhookHandler {
	override val webhookType: WebHookType
		get() = WebHookType.PUSH

	override fun handle(payload: String) {
		println("Handling push webhook")
	}
}

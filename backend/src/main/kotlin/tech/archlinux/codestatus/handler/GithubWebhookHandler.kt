package tech.archlinux.codestatus.handler

import tech.archlinux.codestatus.WebHookType

interface GithubWebhookHandler {
	val webhookType: WebHookType
	suspend fun handle(payload: String)
}

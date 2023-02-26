package tech.archlinux.codestatus.controller

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tech.archlinux.codestatus.service.GithubWebhookService

@RestController
class GithubWebhookController {

	@Autowired
	lateinit var githubWebhookService: GithubWebhookService
	private val log: Logger = LoggerFactory.getLogger(GithubWebhookController::class.java)

	@PostMapping("/event_handler")
	fun handlerWebhook(request: HttpServletRequest, @RequestBody payload: Map<Any, Any>) {
		// 查看所有 header
		request.headerNames.toList().forEach {
			log.info("header: $it -> ${request.getHeader(it)}")
		}

		// 检查 X-Hub-Signature 签名
		val signature = request.getHeader("X-Hub-Signature")

	}
}

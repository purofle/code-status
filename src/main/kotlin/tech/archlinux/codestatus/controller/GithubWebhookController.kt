package tech.archlinux.codestatus.controller

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/event_handler")
class GithubWebhookController {

	private val log: Logger = LoggerFactory.getLogger(GithubWebhookController::class.java)

	@PostMapping("/")
	fun handlerWebhook(request: HttpServletRequest, @RequestBody requestBody: String) {
		// 查看所有 header
		request.headerNames.toList().forEach {
			log.info("header: $it -> ${request.getHeader(it)}")
		}
	}
}

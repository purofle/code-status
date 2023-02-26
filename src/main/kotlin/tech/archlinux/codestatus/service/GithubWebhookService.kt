package tech.archlinux.codestatus.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.archlinux.codestatus.handler.GithubWebhookHandler

@Service
class GithubWebhookService {
	@Autowired
	lateinit var handlers: List<GithubWebhookHandler>
}

package tech.archlinux.codestatus.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.archlinux.codestatus.WebHookType
import tech.archlinux.codestatus.entity.CommitEntity
import tech.archlinux.codestatus.entity.RepositoryEntity
import tech.archlinux.codestatus.pojo.PushEvent
import tech.archlinux.codestatus.repository.AccountRepository
import tech.archlinux.codestatus.repository.CommitRepository
import tech.archlinux.codestatus.repository.RepositoryRepository

@Component
class PushWebhookHandler : GithubWebhookHandler {
    override val webhookType: WebHookType
        get() = WebHookType.PUSH

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var repositoryRepository: RepositoryRepository

    @Autowired
    lateinit var commitRepository: CommitRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    override suspend fun handle(payload: String) = coroutineScope {
        logger.debug("Handling push webhook")

        val pushEvent: PushEvent = objectMapper.readValue(payload)

        pushEvent.apply {
            repository.apply {

                val account = accountRepository.findAccountEntityByNodeId(pushEvent.sender.nodeId)

                logger.debug("Account: {}", account)
                logger.debug("pushEvent: {}", pushEvent)

                val repo = repositoryRepository.findRepositoryEntityByNodeId(nodeId).first() ?: repositoryRepository.save(
                    RepositoryEntity(
                        fullName = fullName,
                        isPrivate = isPrivate,
                        nodeId = nodeId
                    )
                )

                commitRepository.saveAll(
                    commits.map {
                        CommitEntity(
                            userId = account!!,
                            commitId = it.id,
                            timestamp = it.timestamp,
                            message = it.message,
                            url = it.url,
                            addedFiles = (it.added as List<*>).size,
                            removedFiles = (it.removed as List<*>).size,
                            modifiedFiles = (it.modified as List<*>).size,
                            repositoryId = repo
                        )
                    }
                )

            }


            commits.apply {
                logger.debug("Commits: {}", this)
            }
        }

        logger.debug("Push event: {}", pushEvent)
    }
}

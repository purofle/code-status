package tech.archlinux.codestatus.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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

    override fun handle(payload: String) {
        logger.debug("Handling push webhook")

        val pushEvent: PushEvent = objectMapper.readValue(payload)

        pushEvent.apply {
            repository.apply {

                val account = accountRepository.findAccountEntityById(pushEvent.sender.id)

                logger.debug("Account: $account")
                logger.debug("pushEvent: $pushEvent")

                val repo = repositoryRepository.findRepositoryEntityById(id) ?: repositoryRepository.save(
                    RepositoryEntity(
                        id = id,
                        nodeId = nodeId,
                        fullName = fullName,
                        isPrivate = isPrivate,
                        ownerId = account!!
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
                            addedFiles = it.added,
                            removedFiles = it.removed,
                            modifiedFiles = it.modified,
                            repositoryId = repo
                        )
                    }
                )

            }


            commits.apply {
                logger.debug("Commits: $this")
            }
        }

        logger.debug("Push event: $pushEvent")
    }
}

package tech.archlinux.codestatus.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.archlinux.codestatus.entity.CommitEntity
import tech.archlinux.codestatus.entity.RepositoryEntity
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.repository.AccountRepository
import tech.archlinux.codestatus.repository.CommitRepository
import tech.archlinux.codestatus.repository.RepositoryRepository
import tech.archlinux.codestatus.utils.StringUtils.toOffsetDateTime

@Service
class ClientService {

    @Autowired
    lateinit var commitRepository: CommitRepository

    @Autowired
    lateinit var repositoryRepository: RepositoryRepository

    @Autowired
    lateinit var githubAPIService: GithubAPIService

    @Autowired
    lateinit var accountRepository: AccountRepository

    val log = LoggerFactory.getLogger(ClientService::class.java)

    @Transactional
    suspend fun syncCommits(token: String) {
        val recentlyCommit = githubAPIService.recentlyCommit(token)
            .mapNotNull { it.repository }
            .distinct()

        val username = githubAPIService.getUserName(token)

        val user = accountRepository.findAccountEntityByLogin(username.login) ?: throw Exception("User not found")

        val repositories = withContext(Dispatchers.IO) {
            recentlyCommit
                .map {
                    async {
                        log.debug("Saving repository {}...", it.nameWithOwner)
                        repositoryRepository.findRepositoryEntityByFullName(it.nameWithOwner)
                            ?: repositoryRepository.save(
                                RepositoryEntity(
                                    fullName = it.nameWithOwner,
                                    isPrivate = it.isPrivate,
                                    nodeId = it.id
                                )
                            )
                    }
                }
        }.awaitAll()

            val commits = recentlyCommit
                .filter { it.defaultBranchRef?.target?.onCommit?.history?.nodes != null }
                .map {
                    log.debug("Saving commits for repository {}...", it)
                    it.defaultBranchRef?.target?.onCommit?.history?.nodes!!.map { node ->
                        CommitEntity(
                            commitId = node?.oid!!.toString(),
                            timestamp = node.committedDate.toString().toOffsetDateTime(),
                            message = node.message,
                            url = node.commitUrl.toString(),
                            addedFiles = node.additions,
                            removedFiles = node.deletions,
                            modifiedFiles = node.changedFilesIfAvailable ?: 0,
                            nodeId = node.id,
                            userId = user,
                            repositoryId = repositories.first { repository -> repository.fullName == it.nameWithOwner }
                        )
                    }
                }

        commitRepository.saveAll(commits.flatten())

    }

    fun getRanking(token: String): HashMap<Repository, Commit> {
        TODO("Not yet implemented")
    }
}

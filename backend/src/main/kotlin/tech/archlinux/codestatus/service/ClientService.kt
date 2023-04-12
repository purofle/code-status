package tech.archlinux.codestatus.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.repository.AccountRepository
import tech.archlinux.codestatus.repository.CommitRepository
import tech.archlinux.codestatus.repository.RepositoryRepository

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

    suspend fun syncCommits(token: String) {
        val recentlyCommit = githubAPIService.recentlyCommit(token)

        val username = githubAPIService.getUserName(token)

        val user = accountRepository.findAccountEntityByLogin(username.login) ?: throw Exception("User not found")

//        val repositories = recentlyCommit
//            .filterNot { it.repository != null }
//            .map { it.repository!! }
//            .distinct().map {
//            repositoryRepository.findRepositoryEntityByFullName(it.nameWithOwner) ?: repositoryRepository.save(
//                RepositoryEntity(
//                    fullName = it.nameWithOwner,
//                    isPrivate = it.isPrivate,
//                    nodeId = it.id,
//                    ownId =
//                )
//            )
//        }

    }

    fun getRanking(token: String): HashMap<Repository, Commit> {
        TODO("Not yet implemented")
    }
}

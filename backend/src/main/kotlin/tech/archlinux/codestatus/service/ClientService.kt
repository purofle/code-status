package tech.archlinux.codestatus.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

        val user = withContext(Dispatchers.IO) {
            accountRepository.findAccountEntityByLogin(username.login)
        } ?: throw Exception("User not found")

        // 保存数据

    }

    fun getRanking(token: String): HashMap<Repository, Commit> {
        TODO("Not yet implemented")
    }
}

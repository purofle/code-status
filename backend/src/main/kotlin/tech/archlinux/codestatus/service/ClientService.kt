package tech.archlinux.codestatus.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Service
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.repository.CommitRepository

@Service
@CacheConfig(cacheNames = ["client"])
class ClientService {

    @Autowired
    lateinit var commitRepository: CommitRepository

    @Autowired
    lateinit var githubAPIService: GithubAPIService

    fun syncCommits(number: Int) {
        TODO("Not yet implemented")
    }

    fun getRanking(token: String): HashMap<Repository, Commit> {
        return githubAPIService.recentlyCommit(token)
    }
}

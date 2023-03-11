package tech.archlinux.codestatus.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Service
import tech.archlinux.codestatus.repository.CommitRepository

@Service
@CacheConfig(cacheNames = ["client"])
class ClientService {

    @Autowired
    lateinit var commitRepository: CommitRepository

    fun syncCommits(number: Int) {
        TODO("Not yet implemented")
    }

    fun getRanking(): List<Any> {
        return commitRepository.getRanking()
    }
}

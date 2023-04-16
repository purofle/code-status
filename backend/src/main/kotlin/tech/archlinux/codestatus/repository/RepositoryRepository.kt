package tech.archlinux.codestatus.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.RepositoryEntity as GithubRepository

@Repository
interface RepositoryRepository : CoroutineCrudRepository<GithubRepository, Long> {

    fun findRepositoryEntityByNodeId(nodeId: String): Flow<GithubRepository?>
    fun findRepositoryEntityByFullName(fullName: String): Flow<GithubRepository?>
}

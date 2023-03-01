package tech.archlinux.codestatus.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.RepositoryEntity as GithubRepository

@Repository
interface RepositoryRepository : CrudRepository<GithubRepository, Int> {
    fun existsRepositoryById(id: Int): Boolean
}

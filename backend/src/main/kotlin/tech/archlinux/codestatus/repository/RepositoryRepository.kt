package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.RepositoryEntity as GithubRepository

@Repository
interface RepositoryRepository : JpaRepository<GithubRepository, String> {
    fun findRepositoryEntityById(id: String): GithubRepository?
}

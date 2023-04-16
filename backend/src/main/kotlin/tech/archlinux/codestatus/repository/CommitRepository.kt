package tech.archlinux.codestatus.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.CommitEntity

@Repository
interface CommitRepository : CoroutineCrudRepository<CommitEntity, Long> {
}

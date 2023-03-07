package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.CommitEntity

@Repository
interface CommitRepository : JpaRepository<CommitEntity, Int> {

}

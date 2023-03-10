package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.CommitEntity

@Repository
interface CommitRepository : JpaRepository<CommitEntity, Int> {
    // 获取排行榜
    @Query("""select count(*), user_id, repository_id
            from commits
            where timestamp > date_trunc('week', now()) - interval '1 week'
            group by user_id, repository_id
            limit 10
            """, nativeQuery = true)
    fun getRanking(): List<Any>
}

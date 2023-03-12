package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.CommitEntity

@Repository
interface CommitRepository : JpaRepository<CommitEntity, String> {
    // 获取最近一周内的所有提交
    @Query("""select * from commits
            where timestamp > date_trunc('week', now()) - interval '1 week'
            """, nativeQuery = true)
    fun getRecentCommits(): List<CommitEntity>
}

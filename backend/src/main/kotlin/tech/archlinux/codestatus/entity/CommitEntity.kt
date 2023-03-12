package tech.archlinux.codestatus.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "commits")
class CommitEntity(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userId: AccountEntity,

    @Id
    @Column(name = "commit_id")
    @PrimaryKeyJoinColumn
    val commitId: String,

    @Column(name = "timestamp")
    val timestamp: OffsetDateTime,

    @Column(name = "message")
    val message: String,

    @Column(name = "url")
    val url: String,

    @Column(name = "added_files")
    val addedFiles: Int,

    @Column(name = "removed_files")
    val removedFiles: Int,

    @Column(name = "modified_files")
    val modifiedFiles: Int,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id")
    var repositoryId: RepositoryEntity
)

package tech.archlinux.codestatus.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "commits")
class CommitEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "commit_id")
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

    @Column(name = "node_id")
    val nodeId: String? = null,

    @Column(name = "user_id")
    @OneToOne
    val userId: AccountEntity,

    @Column(name = "repository_id")
    @OneToOne
    val repositoryId: RepositoryEntity,

    )

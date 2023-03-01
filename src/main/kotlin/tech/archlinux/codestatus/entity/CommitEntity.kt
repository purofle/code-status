package tech.archlinux.codestatus.entity

import io.hypersistence.utils.hibernate.type.array.ListArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.time.LocalDateTime

@Entity
@Table(name = "commits")
class CommitEntity(
    @OneToOne
    @JoinColumn(name = "user_id")
    val userId: AccountEntity,

    @Id
    @Column(name = "commit_id")
    @PrimaryKeyJoinColumn
    val commitId: String,

    @Column(name = "timestamp")
    val timestamp: LocalDateTime,

    @Column(name = "message")
    val message: String,

    @Column(name = "url")
    val url: String,

    @Column(name = "added_files", columnDefinition = "text[]")
    @Type(ListArrayType::class)
    val addedFiles: List<String>,

    @Column(name = "removed_files", columnDefinition = "text[]")
    @Type(ListArrayType::class)
    val removedFiles: List<String>,

    @Column(name = "modified_files", columnDefinition = "text[]")
    @Type(ListArrayType::class)
    val modifiedFiles: List<String>,

    @OneToOne
    @JoinColumn(name = "repository_id")
    var repositoryId: RepositoryEntity
)

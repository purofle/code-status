package tech.archlinux.codestatus.entity

import io.hypersistence.utils.hibernate.type.array.ListArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.time.LocalDateTime

@Entity
@Table(name = "push_event")
class PushEvent(
    @OneToOne
    @JoinColumn(name = "user_id")
    val userId: Account,

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
    val addedFiles: Array<String>,

    @Column(name = "removed_files", columnDefinition = "text[]")
    @Type(ListArrayType::class)
    val removedFiles: Array<String>,

    @Column(name = "modified_files", columnDefinition = "text[]")
    @Type(ListArrayType::class)
    val modifiedFiles: Array<String>,

    @OneToOne
    @JoinColumn(name = "repository_id")
    var repositoryId: Repository
)

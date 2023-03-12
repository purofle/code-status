package tech.archlinux.codestatus.entity

import jakarta.persistence.*

@Entity
@Table(name = "repository", schema = "public", catalog = "codestatus")
class RepositoryEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "full_name")
    var fullName: String,

    @Column(name = "private")
    val isPrivate: Boolean,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val ownerId: AccountEntity
)

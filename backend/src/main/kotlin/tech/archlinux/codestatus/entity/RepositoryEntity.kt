package tech.archlinux.codestatus.entity

import jakarta.persistence.*

@Entity
@Table(name = "repository", schema = "public", catalog = "codestatus")
class RepositoryEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "full_name")
    var fullName: String,

    @Column(name = "private")
    val isPrivate: Boolean,

    @JoinColumn(name = "owner_id")
    @OneToOne
    val ownId: AccountEntity,

    @Column(name = "node_id")
    val nodeId: String,

    )

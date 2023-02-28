package tech.archlinux.codestatus.entity

import jakarta.persistence.*

@Entity
@Table(name = "repository", schema = "public", catalog = "codestatus")
class Repository(
    @Id
    @Column(name = "id")
    val id: Int,

    @Column(name = "node_id")
    var nodeId: String,

    @Column(name = "full_name")
    var fullName: String,

    @Column(name = "private")
    val isPrivate: Boolean,

    @OneToOne
    @JoinColumn(name = "owner_id")
    val ownerId: Account
)

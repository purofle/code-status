package tech.archlinux.codestatus.entity

import jakarta.persistence.*


@Entity
@Table(name = "account")
class AccountEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "node_id")
    val nodeId: String,

    @Column(name = "avatar_url")
    val avatarUrl: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "login")
    val login: String,
)

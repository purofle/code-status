package tech.archlinux.codestatus.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "account")
class AccountEntity(
    @Id val id: Int,
    @Column(name = "node_id") val nodeId: String,
    @Column(name = "avatar_url") val avatarUrl: String,
    val email: String,
    val name: String,
)

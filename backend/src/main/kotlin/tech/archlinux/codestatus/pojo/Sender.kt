package tech.archlinux.codestatus.pojo

import com.fasterxml.jackson.annotation.JsonProperty


data class Sender(
    val login: String,
    val id: Int,
    @JsonProperty("node_id") val nodeId: String,
    @JsonProperty("avatar_url") val avatarUrl: String,
)

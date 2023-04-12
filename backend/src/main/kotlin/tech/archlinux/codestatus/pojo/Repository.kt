package tech.archlinux.codestatus.pojo

import com.fasterxml.jackson.annotation.JsonProperty

data class Repository(
    val id: Int,
    @JsonProperty("node_id") val nodeId: String,
    @JsonProperty("full_name") val fullName: String,
    @JsonProperty("private") val isPrivate: Boolean,
    val owner: UserLogin
)

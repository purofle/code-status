package tech.archlinux.codestatus.pojo

import com.fasterxml.jackson.annotation.JsonProperty

data class Commit(
    val id: String,
    @JsonProperty("tree_id") val treeId: String,
    val message: String,
    val timestamp: String,
    val url: String,
    val added: List<String>,
    val removed: List<String>,
    val modified: List<String>
)

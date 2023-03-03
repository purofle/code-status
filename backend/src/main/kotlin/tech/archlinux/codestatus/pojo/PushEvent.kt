package tech.archlinux.codestatus.pojo

data class PushEvent(
    val repository: Repository,
    val sender: Sender,
    val pusher: Map<String, String>,
    val commits: List<Commit>
)

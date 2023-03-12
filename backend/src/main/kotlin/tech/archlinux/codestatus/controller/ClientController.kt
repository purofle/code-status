package tech.archlinux.codestatus.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.service.ClientService

@RestController
@RequestMapping("/api")
class ClientController {

    @Autowired
    lateinit var clientService: ClientService

    @GetMapping("/user")
    fun user(
        @RequestAttribute("user") user: String
    ): String {
        return user
    }

    /**
     * 同步最近的提交记录
     */
    @GetMapping("/sync")
    fun sync(
        @RequestHeader("Authorization") token: String
    ) {
        clientService.syncCommits(token)
    }

    /**
     * 获取排行榜
     */
    @GetMapping("/ranking")
    fun ranking(
        @RequestHeader("Authorization") token: String,
    ): HashMap<Repository, Commit> {
        return clientService.getRanking(token)
    }

}

package tech.archlinux.codestatus.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.archlinux.codestatus.pojo.Commit
import tech.archlinux.codestatus.pojo.Repository
import tech.archlinux.codestatus.pojo.UserLogin
import tech.archlinux.codestatus.service.ClientService

@RestController
@RequestMapping("/api")
class ClientController {

    @Autowired
    lateinit var clientService: ClientService

    /**
     * 获取当前 Token 对应的用户信息
     * @param token GitHub Token
     * @return 用户信息
     */
    @GetMapping("/user")
    suspend fun getMe(
        @RequestHeader("Authorization") token: String,
    ): UserLogin {
        return clientService.githubAPIService.getUserName(token)
    }

    /**
     * 同步最近的提交记录
     * @param token GitHub Token
     */
    @GetMapping("/sync")
    suspend fun sync(
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

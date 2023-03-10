package tech.archlinux.codestatus.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
        number: Int
    ) {
        clientService.syncCommits(number)
    }

    /**
     * 获取排行榜
     */
    @GetMapping("/ranking")
    fun ranking(): List<Any> {
        return clientService.getRanking()
    }

}

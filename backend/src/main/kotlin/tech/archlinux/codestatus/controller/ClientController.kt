package tech.archlinux.codestatus.controller

import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ClientController {

    @RequestMapping("/user")
    fun user(
        @RequestAttribute("user") user: String
    ): String {
        return user
    }
}

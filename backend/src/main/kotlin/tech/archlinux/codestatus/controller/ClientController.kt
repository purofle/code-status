package tech.archlinux.codestatus.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.archlinux.codestatus.service.ClientService

@RestController
@RequestMapping("/api")
class ClientController {
    @Autowired
    lateinit var clientService: ClientService

    @RequestMapping("/**")
    fun index(): String {
        return "Hello World"
    }
}

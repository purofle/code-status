package tech.archlinux.codestatus.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class GithubAPIServiceTest {

    @Value("\${github.access_token}")
    lateinit var accessToken: String


    @Autowired
    lateinit var githubAPIService: GithubAPIService

    @Test
    fun recentlyCommit() {
        println(githubAPIService.recentlyCommit(accessToken))
    }
}

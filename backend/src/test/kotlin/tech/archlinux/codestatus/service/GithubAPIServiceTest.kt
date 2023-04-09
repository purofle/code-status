package tech.archlinux.codestatus.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun recentlyCommit() = runTest {
        println(githubAPIService.recentlyCommit(accessToken))
    }
}

package tech.archlinux.codestatus

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringBootTest
@AutoConfigureMockMvc
class CodeStatusApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `test event_handler and sign`() {
        MockMvcRequestBuilders.post("/event_handler").apply {
            header("X-GitHub-Event", "push")
            header("X-Hub-Signature-256", "sha256=bca046e3b246b332537762f830ab46d1d24710d12b8109716c3e9bbd684b6a10")
            header("content-type", "application/json")
            // 读取 resources 下的 testBody.json 作为请求体
            val resource =
                javaClass.classLoader.getResource("testBody.json")?.file ?: throw Exception("testBody.json not found")
            val body = java.io.File(resource).readText()
            content(body)
        }.let {
            mockMvc.perform(it)
        }
    }

}

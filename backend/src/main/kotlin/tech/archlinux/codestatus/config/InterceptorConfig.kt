package tech.archlinux.codestatus.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    @Autowired
    lateinit var githubSignInterceptor: GithubSignInterceptor

    @Autowired
    lateinit var apiInterceptor: APIInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(githubSignInterceptor)
            .addPathPatterns(AppConfig.webhook)
        registry.addInterceptor(apiInterceptor)
            .addPathPatterns("/api/**")
    }
}

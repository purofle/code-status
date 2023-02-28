package tech.archlinux.codestatus.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubUtils {

    @Value("\${github.secret}")
    lateinit var secret: String

    @Value("\${github.debug}")
    var debug: Boolean = false

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    fun validateSignature(signature: String, payload: String): Boolean {

        val sha256 = "sha256=${HMAC.calculateHMac(secret = secret, data = payload)}"
        logger.info("payload: $payload")
        logger.info("sha256: $sha256")
        logger.info("signature: $signature")
        return sha256 == signature
    }
}

package tech.archlinux.codestatus.utils

import org.apache.commons.codec.binary.Hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMAC {
	/**
	 * 计算 HMAC_SHA256
	 * @param algorithm 算法
	 * @param secret 密钥
	 */
	fun calculateHMac(
        algorithm: String = "HmacSHA256",
        secret: String,
        data: ByteArray
	): String {
		val hmac = Mac.getInstance(algorithm)
		// 初始化 hmac
		hmac.init(SecretKeySpec(secret.encodeToByteArray(), algorithm))

        return Hex.encodeHexString(hmac.doFinal(data))
	}
}

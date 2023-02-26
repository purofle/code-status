package tech.archlinux.codestatus.utils

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
		data: String
	): String {
		val hmac = Mac.getInstance(algorithm)
		// 初始化 hmac
		hmac.init(SecretKeySpec(secret.encodeToByteArray(), algorithm))

		return byteArrayToHex(hmac.doFinal(data.encodeToByteArray()))
	}

	private fun byteArrayToHex(ba: ByteArray): String {
		val sb = StringBuilder(ba.size * 2)
		ba.forEach { b ->
			sb.append(String.format("%02x", b))
		}
		return sb.toString()
	}
}

package tech.archlinux.codestatus.utils

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils

object HMAC {
    /**
     * 计算 HMAC_SHA256
     * @param algorithm 算法
     * @param secret 密钥
     */
    fun calculateHMac(
        algorithm: HmacAlgorithms = HmacAlgorithms.HMAC_SHA_256,
        secret: String,
        data: String
    ): String {

        val mac = HmacUtils.getInitializedMac(algorithm, secret.encodeToByteArray())
        return Hex.encodeHexString(mac.doFinal(data.encodeToByteArray()))
    }
}

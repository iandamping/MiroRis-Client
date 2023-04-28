package com.spe.miroris.security.hmac

import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class HmacManagerImpl @Inject constructor():HmacManager {

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class
    )
    override fun computeHMACSHA512(hmacKey: String, value: String): String {
        val key = SecretKeySpec(hmacKey.toByteArray(), "HmacSHA512")
        val mac: Mac = Mac.getInstance("HmacSHA512")
        mac.init(key)
        val bytes: ByteArray = mac.doFinal(value.toByteArray())
        val hexString = StringBuilder()

        for (aByte in bytes) {
            val hex = Integer.toHexString(0xff and aByte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return String(hexString)
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class
    )
    override fun computeHMACSHA512Byte(hmacKey: String, value: String): ByteArray {
        val byteKey = hmacKey.toByteArray(StandardCharsets.UTF_8)
        val mac: Mac = Mac.getInstance("HmacSHA512")
        val keySpec = SecretKeySpec(byteKey, "HmacSHA512")
        mac.init(keySpec)
        return mac.doFinal(value.toByteArray(StandardCharsets.UTF_8))
    }
}
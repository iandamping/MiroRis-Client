package com.spe.miroris.security.ftaes

import android.annotation.SuppressLint
import android.util.Base64
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class FTAesImpl @Inject constructor(): FTAes {

    override fun encrypt(input: String, aesKey: String, ivKey: String): String {
        var crypted: ByteArray? = null
        try {
            val e = SecretKeySpec(aesKey.toByteArray(), "AES")
            val iv = ivKey.toByteArray(Charsets.UTF_8)
            val ivParameterSpec = IvParameterSpec(iv)

            @SuppressLint("GetInstance") val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(1, e, ivParameterSpec)
            crypted = cipher.doFinal(input.toByteArray())
        } catch (var5: Exception) {
            Timber.e("encrypt failed : ${var5.message}")
        }

        return if (crypted != null) {
            String(Base64.encode(crypted, Base64.NO_WRAP))
        } else {
            ""
        }
    }

    /**this is AES_256_CBC with IVkey*/
    override fun decrypt(input: String, aesKey: String, ivKey: String): String {
        var output: ByteArray? = null
        try {
            val iv = ivKey.toByteArray(Charsets.UTF_8)
            val ivParameterSpec = IvParameterSpec(iv)

            val e = SecretKeySpec(aesKey.toByteArray(Charsets.UTF_8), "AES")
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

            cipher.init(Cipher.DECRYPT_MODE, e, ivParameterSpec)
            output = cipher.doFinal(Base64.decode(input, Base64.NO_WRAP))
        } catch (var5: Exception) {
            Timber.e("decrypt failed : ${var5.message}")
        }

        return output?.let { String(it) } ?: ""
    }
}

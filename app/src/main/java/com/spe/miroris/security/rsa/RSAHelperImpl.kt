package com.spe.miroris.security.rsa

import android.util.Base64
import timber.log.Timber
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class RSAHelperImpl @Inject constructor(): RSAHelper {
    companion object {
        private const val TAG = "Rsa Helper"
    }

    override fun encrypt(publicKey: String, data: String): String {
        var encrypted = ByteArray(0)
        try {
            val publicBytes: ByteArray =
                Base64.decode(publicKey, Base64.NO_WRAP)
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val pubKey = keyFactory.generatePublic(keySpec)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING") //or try with "RSA"
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            encrypted = cipher.doFinal(data.toByteArray())
        } catch (e: Exception) {
            Timber.tag(TAG).e("encrypt rsa failed  : ${e.message}")
        }
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)


    }

    override fun decrypt(privateKey: String, data: String?): String {
        var decodedData = ByteArray(0)
        try {
            val publicBytes: ByteArray =
                Base64.decode(privateKey, Base64.NO_WRAP)
            val keySpec =
                X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val pubKey = keyFactory.generatePublic(keySpec)
            val cipher =
                Cipher.getInstance("RSA/ECB/PKCS1PADDING") //or try with "RSA"
            cipher.init(Cipher.DECRYPT_MODE, pubKey)
            decodedData = cipher.doFinal(Base64.decode(data, Base64.NO_WRAP))
        } catch (e: Exception) {
            Timber.tag(TAG).e("decrypt rsa failed  : ${e.message}")
        }
        return String(decodedData)

    }
}

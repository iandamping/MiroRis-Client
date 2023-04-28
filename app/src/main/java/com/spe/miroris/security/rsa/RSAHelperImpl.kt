package com.spe.miroris.security.rsa

import android.util.Base64
import timber.log.Timber
import javax.inject.Inject

class RSAHelperImpl @Inject constructor(): RSAHelper {
    companion object {
        private const val TAG = "EncryptionManager"
    }

    override fun encrypt(publicKey: String, data: String): String {
        var base64 = ""
        try {
            val result: ByteArray = RSAAndroid.encode(data.toByteArray(), publicKey)
            base64 = String(Base64.encode(result, Base64.NO_WRAP))
        } catch (e: RSAException) {
            Timber.tag(TAG).e("encryptRsa failed : %s", e.message)
        }
        return base64
    }

    override fun decrypt(privateKey: String, data: String?): String {
        var decodeString = ""
        try {
            val decodeBase64Bytes =
                Base64.decode(requireNotNull(data) {
                    "Data to decrypt is null !"
                }, Base64.NO_WRAP)

            val decodeBytes: ByteArray = RSAAndroid.decode(decodeBase64Bytes, privateKey)
            decodeString = String(decodeBytes)
        } catch (e: RSAException) {
            Timber.tag(TAG).e("decryptRsa failed : %s", e.message)
        }
        return decodeString
    }
}

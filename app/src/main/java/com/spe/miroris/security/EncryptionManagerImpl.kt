package com.spe.miroris.security

import android.os.Build
import android.util.Base64
import com.spe.miroris.security.ftaes.FTAes
import com.spe.miroris.security.hmac.HmacManager
import com.spe.miroris.security.keyProvider.KeyProvider
import com.spe.miroris.security.rsa.RSAHelper
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import javax.inject.Inject

class EncryptionManagerImpl @Inject constructor(
    private val ftAes: FTAes,
    private val rsaHelper: RSAHelper,
    private val hmacManager: HmacManager,
    private val keyProvider: KeyProvider
) :
    EncryptionManager {
    companion object {
        private const val TAG = "EncryptionManager"
    }


    override fun encryptAes(input: String): String {
        return ftAes.encrypt(input, keyProvider.provideAES(), keyProvider.provideIvAES())
    }

    override fun decryptAes(input: String): String {
        return ftAes.decrypt(input, keyProvider.provideAES(), keyProvider.provideIvAES())
    }

    override fun encryptRsa(data: String): String {
        return rsaHelper.encrypt(keyProvider.providePublicRsa(), data)
    }

    override fun decryptRsa(data: String): String {
        //we are using public key because of mobile-api setup
        return rsaHelper.decrypt(keyProvider.providePublicRsa(),data)
    }

    override fun createHmacSignature(value: String): String {
        var hmacResult = ""
        try {
            hmacResult =
                Base64.encodeToString(
                    hmacManager.computeHMACSHA512(keyProvider.provideHMAC(), value).toByteArray(
                        StandardCharsets.UTF_8
                    ),
                    Base64.NO_WRAP
                )
        } catch (e: Exception) {
            Timber.tag(TAG).e("createHmacSignature failed : %s", e.message)
        }
        return hmacResult
    }

    override fun signSHA256RSA(input: String, key: String): String {
        val b1: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getDecoder().decode(key)
        } else {
            Base64.decode(key, Base64.NO_WRAP)
        }
        val spec = PKCS8EncodedKeySpec(b1)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")

        val privateSignature: Signature = Signature.getInstance("SHA256withRSA")
        privateSignature.initSign(kf.generatePrivate(spec))
        privateSignature.update(input.toByteArray(Charsets.UTF_8))
        val s: ByteArray = privateSignature.sign()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(s)
        } else {
            Base64.encodeToString(s, 0)
        }
    }


    @Throws(NoSuchAlgorithmException::class)
    override fun sha256WithBytesToHex(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(value.toByteArray())

        val result = StringBuffer()
        for (aByte in md.digest()) {
            val hex = Integer.toHexString(0xff and aByte.toInt())
            if (hex.length == 1) result.append('0')
            result.append(hex)
        }
        return result.toString()
    }




}
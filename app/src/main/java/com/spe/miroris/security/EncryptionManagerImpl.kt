package com.spe.miroris.security

import android.os.Build
import android.util.Base64
import com.spe.miroris.security.ftaes.FTAes
import com.spe.miroris.security.hmac.HmacManager
import com.spe.miroris.security.keyProvider.KeyProvider
import com.spe.miroris.security.rsa.RSAHelper
import timber.log.Timber
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
        return rsaHelper.encrypt(keyProvider.provideRsa(), data)
    }

    override fun decryptRsa(privateKey: String, data: String?): String {
        return rsaHelper.decrypt(privateKey, data)
    }

    override fun createHmacSignature(value: String): String {
        var hmacResult = ""
        try {
            hmacResult =
                Base64.encodeToString(
                    hmacManager.computeHMACSHA512Byte(keyProvider.provideHMAC(), value),
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

    override fun provideHmac512Key(): String {
        return "Hk?H67>MXaRD"
    }

    override fun provideRsaPublicKey(): String {
        return "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAqRpvWSazwzXt01sxlquF\n" +
                "H5Gy50Fybd1qX4jgT4oF042X7gUBxvBi7ryiuoAI1WbxZhOlu3MKD1kCDBJJy0NU\n" +
                "fxVMFu6zrZog+ZDvvPP9UsDu/7StVaF7hzF0p1ZIqw1ZZKORBjq3enTvqS6cDi0A\n" +
                "sB/D5aVKF6BgF33ULxTkXXiu5OQv3/Y2OZr5Py0cbMvxswhNPLVCWt4+48JuGp+S\n" +
                "CRYOLYDW6u3vjdMgzXYA9VNDDOUnHy2gI//BWWygThAlilaFCurUGEOwxD8wYBXg\n" +
                "8dPhwZ4juBFcFmRI4Oa51si5zSHOMCYJ7EfSLkLT4CUIvMonkAr2R50fFAf4nmil\n" +
                "C0M1ZzvB4rmfZuHzBz0pNDxOi7awB+fDQ/oYkb6RPSEnugmTuhDy0wTXX4b+vdEq\n" +
                "K3V9GTboGs8KAbpOUtkLZYWP956lrTMFS/h5A5zzoLCHVvOiW4KkkPaj0tbLqzHM\n" +
                "hpeAu2/x/Z2ASQ0/wupGcdqp+6YuO0Q8CwVcZiicpeftAgMBAAE=\n"
    }

    override fun provideAesKey(): String {
        return "yM2Kh&hkGH8JKMNR6GTJKRdFGBvFUIKM"
    }


    override fun provideAesIVKey(): String {
        return "jKL67hJLArcT9Hnm"
    }

    override fun provideClientSecret(): String {
        return "ChT4D?JyiXaD"
    }

    override fun provideClientId(): String {
        return "2023"
    }

    override fun provideAuthVersion(): String {
        return "V1.0.0"
    }


}
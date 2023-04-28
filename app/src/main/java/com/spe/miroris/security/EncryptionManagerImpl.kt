package com.spe.miroris.security

import android.os.Build
import android.util.Base64
import com.spe.miroris.security.ftaes.FTAes
import com.spe.miroris.security.hmac.HmacManager
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
    private val hmacManager: HmacManager
) :
    EncryptionManager {
    companion object {
        private const val TAG = "EncryptionManager"
    }

    override fun encryptAes(input: String, aesKey: String, ivKey: String): String {
        return ftAes.encrypt(input, aesKey, ivKey)
    }

    override fun decryptAes(input: String, aesKey: String, ivKey: String): String {
        return ftAes.decrypt(input, aesKey, ivKey)
    }

    override fun encryptRsa(publicKey: String, data: String): String {
        return rsaHelper.encrypt(publicKey, data)
    }

    override fun decryptRsa(privateKey: String, data: String?): String {
        return rsaHelper.decrypt(privateKey, data)
    }

    override fun createHmacSignature(hmacKey: String, value: String): String {
        var hmacResult = ""
        try {
            hmacResult =
                Base64.encodeToString(
                    hmacManager.computeHMACSHA512Byte(hmacKey, value),
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

    override fun provideRsaPrivateKey(): String {
        return "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDdrnN/gnTjY4hFfFjdooeAGOg0kGrzfzgNsuLzh0nymr2rnNRpBjMdD8NDO3qiD5D7KP2Z973RfkA7PshMriEfK5OH05OIZ2gRWplXgyTmQJIrSPawIL50xpI35T/nvad4MXerH8C9qrNiQAjGuRL2CkEB4aTDuNF4MSmjsIb6im3uaFfx6JU1fBVovfMiNWeFT+s5JFUaVLgK0AoziYgTg1zGHBOilKq5u4FSQjnLXILu2Q3itly1pStB59oX3vxCrncgiYgcx1/MO7Xym5JifXYO0orgGuC+XkqmamqRQHycOkdjvwr70okWdpqYvwRaiyqrZ6UF3R4ToCFLLF3dAgMBAD8CggEAK4L1sx1hc4RLwn/7Z7bNUhXRWnR/87drtAGxl4OyUKHqfG+JsXqcZNAeVbGsE9ecXLmBQk1hFnDf3/usA2nb8UMccW7ZZAwv7KOYP3+h2k8EtYvhmOBjqqLYPXewHJTUc38NvjNHq7A17n8i39Pd09MtQvbl8CTpw+2zLvIcQ+qXiRHzB7gCfmGj/Snsdjz/2qczy0w6neLWx2Y0tAxci1eMo6iu1luH/M/jyzqBnmu77MXSXDTSySkHrfu0AuhYDYpEDTrNhyfQD4exN/Lg7detPme8Myedk1mdYXGqHzPcoGUZluu11Qk84/Vt4q2QsQbZx7RFJj4iNH5+iHnZdwKBgQD2xd/IGxUhPWJcxrFezw+Gc8fnkyjRU9Uq5iQOjd/z08Wu0vFQ6h+2uDdwGWbXePmwAAMoFBIxwW4zsBX15jIJh7SQtGxTQaQt61/gxR73ik24GPcdp9jBm9J6c4wjFjYiL4CfLTng9HGFePcd3Ok+6Y2k8VJCvuOhUA+kyRqeuQKBgQDl+GZQyq9Fhit8WW8x+SYNBBxJtn1Ww/lsTXCkXkbG7oUQ2qr4S7mDHQu9gwH8PA3EwGDwYVAf2+SZVvzX1oiktTptOZtYUkkwFZYeOWNrfupmprqDCUg+b+fifKsreFNg1oSINoXsQ3/3uMQcRnFaOSM1w55oYYQ/C8JHR+FGRQKBgADjRnvz9+13NEDRk5gw8taNJf/vf7kyZeFxEVX5QnMac7fC8X0I0qlkwgdbh+RdcId86EW/yE56jiibqAXlPMwqiqJXcs6b6CU5K+qUey470sbGzBeViO8fotz0glkcanuzErj3ONpdpE04Crkr9dWGczg5OGL1LaOdcg9aIjYfAoGBAMq88JIhFMwka1MeWvXfkD5HCBdSKpvfLSIlYq3SZn97xLh4/82NBKx7ZCmB7vurcxkGdjOnuPTIxe1WaXPmyuIiQXOcvPFNonhRwEyy1id809GalVS3jLNwiRaQY2cylWiUL5WZBD9z5tth7vv/aVWojQZR45ar0PFS1fpL5i1jAoGBALm0Uayl5H3G98vCl5wSkBekomykSfRCbT65dpWEGDkscHrMwMd9jOdPRrZKG5Q0qnmSRW7rrKr16RyJtGwq9yZ2YLkx6HAgc929IBtgn8/akZ7e3BZdQC607/EbGgfXbDweHja2BBT6cusgTv5fjPZFXqCqkxZuew7RYcE+Bz9S"
    }

    override fun provideClientKey(): String {
        return "MOBILE"
    }

    override fun provideClientSecret(): String {
        return "3db4d061aa9830d72143998s2013is90"
    }

    override fun provideClientIV(): String {
        return "3db4d061aa9830d7"
    }


}
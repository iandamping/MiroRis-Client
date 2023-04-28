package com.spe.miroris.security.rsa

import android.util.Base64
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Security
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Encryption and decryption for rsa
 *
 * @author zhongyongsheng
 */
object RSAAndroid {
    private const val KEY_ALGORITHM = "RSA"
    private const val RSA_ALGORITHM = "RSA/ECB/PKCS1Padding"
    private const val BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"
    private const val END_PUBLIC_KEY = "-----END PUBLIC KEY-----"
    private const val PROVIDER = "BC"
    var DEBUG = true

    @Throws(RSAException::class)
    fun openSSLPemStringToPublicKey(data: String?): PublicKey? {
        var value = data ?: return null
        value = value.replace(BEGIN_PUBLIC_KEY, "")
        value = value.replace(END_PUBLIC_KEY, "")
        return base64StringToPublicKey(value)
    }

    @Throws(RSAException::class)
    fun base64StringToPublicKey(value: String?): PublicKey {
        return try {
            val keyBytes = Base64.decode(value, Base64.DEFAULT)
            bytesToPublicKey(keyBytes)
        } catch (e: Exception) {
            throw RSAException(e)
        }
    }

    @Throws(RSAException::class)
    fun bytesToPublicKey(value: ByteArray?): PublicKey {
        return try {
            val keyFactory =
                KeyFactory.getInstance(KEY_ALGORITHM)
            val oX509EncodedKeySpec =
                X509EncodedKeySpec(value)
            keyFactory.generatePublic(oX509EncodedKeySpec)
        } catch (e: Exception) {
            throw RSAException(e)
        }
    }

    @Throws(RSAException::class)
    fun openSSLPemStringToPrivateKey(data: String?): PublicKey? {
        var value = data ?: return null
        value = value.replace(BEGIN_PUBLIC_KEY, "")
        value = value.replace(END_PUBLIC_KEY, "")
        return base64StringToPublicKey(value)
    }

    @Throws(RSAException::class)
    fun encode(value: ByteArray, publicKey: String?): ByteArray {
        val key = openSSLPemStringToPublicKey(publicKey)
        return encode(value, key)
    }

    @Throws(RSAException::class)
    fun encode(value: ByteArray, publicKey: PublicKey?): ByteArray {
        return try {
            val provider =
                Security.getProvider(PROVIDER)
            val cipher =
                Cipher.getInstance(RSA_ALGORITHM, provider)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            segmentProcess(cipher, value, cipher.blockSize)
        } catch (e: Exception) {
            throw RSAException(e)
        }
    }

    @Throws(RSAException::class)
    fun decode(value: ByteArray, privateKey: String?): ByteArray {
        val key = openSSLPemStringToPrivateKey(privateKey)
        return decode(value, key)
    }

    @Throws(RSAException::class)
    fun decode(value: ByteArray, privateKey: PublicKey?): ByteArray {
        return try {
            val provider =
                Security.getProvider(PROVIDER)
            val cipher =
                Cipher.getInstance(RSA_ALGORITHM, provider)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            segmentProcess(cipher, value, cipher.blockSize)
        } catch (e: Exception) {
            throw RSAException(e)
        }
    }

    @Throws(RSAException::class)
    private fun segmentProcess(
        cipher: Cipher,
        result: ByteArray,
        segmentLength: Int
    ): ByteArray {
        var data = result
        val baos = ByteArrayOutputStream()
        return try {
            val inputLength = data.size
            var offset = 0
            var i = 0
            while (inputLength - offset > 0) {
                val cache: ByteArray = if (inputLength - offset > segmentLength) {
                    cipher.doFinal(data, offset, segmentLength)
                } else {
                    cipher.doFinal(data, offset, inputLength - offset)
                }
                baos.write(cache, 0, cache.size)
                i++
                offset = i * segmentLength
            }
            data = baos.toByteArray()
            data
        } catch (e: Exception) {
            throw RSAException(e)
        } finally {
            try {
                baos.close()
            } catch (e: IOException) {
                Timber.tag("Rsa Android").e("segmentProcess failed: %s", e.message)
            }
        }
    }
}

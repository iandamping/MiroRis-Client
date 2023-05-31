package com.spe.miroris.security.keyProvider

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.spe.miroris.core.data.dataSource.cache.preference.PreferenceHelper
import timber.log.Timber
import java.security.KeyStore
import java.security.SecureRandom
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class KeyProviderImpl @Inject constructor(private val preferenceHelper: PreferenceHelper) :
    KeyProvider {

    companion object {
        const val PUBLIC_RSA_KEY_1 = "A1"
        const val PUBLIC_RSA_KEY_2 = "B1"
        const val PUBLIC_RSA_KEY_3 = "C1"
        const val PUBLIC_RSA_KEY_4 = "D1"
        const val PUBLIC_RSA_KEY_5 = "E1"
        const val PUBLIC_RSA_KEY_6 = "F1"
        const val HMAC_KEY_1 = "A2"
        const val HMAC_KEY_2 = "B2"
        const val HMAC_KEY_3 = "C2"
        const val AES_KEY_1 = "A3"
        const val AES_KEY_2 = "B3"
        const val AES_KEY_3 = "C3"
        const val AES_KEY_4 = "D3"
        const val IV_AES_KEY_1 = "A4"
        const val IV_AES_KEY_2 = "B4"
        const val CLIENT_SECRET_KEY_1 = "A5"
        const val CLIENT_SECRET_KEY_2 = "B5"
        const val PRIVATE_RSA_KEY_1 = "A6"
        const val PRIVATE_RSA_KEY_2 = "B6"
        const val PRIVATE_RSA_KEY_3 = "C6"
        const val PRIVATE_RSA_KEY_4 = "D6"
        const val PRIVATE_RSA_KEY_5 = "E6"
        const val PRIVATE_RSA_KEY_6 = "F6"
        const val PRIVATE_RSA_KEY_7 = "G6"
        const val PRIVATE_RSA_KEY_8 = "H6"
        const val PRIVATE_RSA_KEY_9 = "I6"
        const val PRIVATE_RSA_KEY_10 = "J6"
    }

    private fun generateSecretKey(key: String): HashMap<String, ByteArray> {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "MyKeyAlias",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true) // 4 different ciphertext for same plaintext on each call
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()

        return keystoreEncrypt(key.toByteArray(Charsets.UTF_8))
    }

    private fun getSecretKey(data: HashMap<String, ByteArray>): String {
        val decryptedBytes = keystoreDecrypt(data)
        return try {
            checkNotNull(decryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            Timber.e("secret key failed because : $e")
            ""
        }
    }

    private fun keystoreDecrypt(map: HashMap<String, ByteArray>): ByteArray? {
        var decrypted: ByteArray? = null
        try {
            // 1
            // Get the key
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val secretKeyEntry = keyStore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
            val secretKey = secretKeyEntry.secretKey

            // 2
            // Extract info from map
            val encryptedBytes = map["encrypted"]
            val ivBytes = map["iv"]

            // 3
            // Decrypt data
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            decrypted = cipher.doFinal(encryptedBytes)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return decrypted
    }

    private fun keystoreEncrypt(dataToEncrypt: ByteArray): HashMap<String, ByteArray> {
        val map = HashMap<String, ByteArray>()
        try {

            // 1
            // Get the key
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val secretKeyEntry =
                keyStore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
            val secretKey = secretKeyEntry.secretKey

            // 2
            // Encrypt data
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val ivBytes = cipher.iv
            val encryptedBytes = cipher.doFinal(dataToEncrypt)

            // 3
            map["iv"] = ivBytes
            map["encrypted"] = encryptedBytes
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return map
    }

    private fun encryptValue(
        plainTextBytes: ByteArray,
        passwordString: String
    ): HashMap<String, ByteArray> {
        val map = HashMap<String, ByteArray>()
        try {
            // Random salt for next step
            val random = SecureRandom()
            val salt = ByteArray(256)
            random.nextBytes(salt)

            // PBKDF2 - derive the key from the password, don't use passwords directly
            val passwordChar = passwordString.toCharArray() // Turn password into char[] array

            val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256) // 1324 iterations
            val secretKeyFactory: SecretKeyFactory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes: ByteArray = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")

            // Create initialization vector for AES
            val ivRandom = SecureRandom() // not caching previous seeded instance of SecureRandom
            val iv = ByteArray(16)
            ivRandom.nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            // Encrypt
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted: ByteArray = cipher.doFinal(plainTextBytes)

            map["salt"] = salt
            map["iv"] = iv
            map["encrypted"] = encrypted
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }

    private fun decryptValue(map: HashMap<String, ByteArray>, passwordString: String): ByteArray? {
        var decrypted: ByteArray? = null
        try {
            require(passwordString != "")
            val salt = map["salt"]
            val iv = map["iv"]
            val encrypted = map["encrypted"]

            // regenerate key from password
            val passwordChar = passwordString.toCharArray()

            val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256)
            val secretKeyFactory: SecretKeyFactory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes: ByteArray = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")

            // Decrypt
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            decrypted = cipher.doFinal(encrypted)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return decrypted
    }


    private fun getValue(secretKey: String, value: HashMap<String, ByteArray>): String {
        val decrypted: ByteArray? = decryptValue(value, secretKey)
        return if (decrypted != null) {
            String(decrypted)
        } else return ""
    }

    private fun generateValueForPublicRsa1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek =
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAqRpvWSazwzXt01sxlquFH5Gy50Fybd1qX4jgT4oF042X7gUBxvBi7ry"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPublicRsa2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek =
            "iuoAI1WbxZhOlu3MKD1kCDBJJy0NUfxVMFu6zrZog+ZDvvPP9UsDu/7StVaF7hzF0p1ZIqw1ZZKORBjq3enTvqS6cDi0AsB/D5"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPublicRsa3(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek =
            "aVKF6BgF33ULxTkXXiu5OQv3/Y2OZr5Py0cbMvxswhNPLVCWt4+48JuGp+SCRYOLYDW6u3vjdMgzXYA9VNDDOUnHy2gI//BWWyg"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPublicRsa4(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek =
            "ThAlilaFCurUGEOwxD8wYBXg8dPhwZ4juBFcFmRI4Oa51si5zSHOMCYJ7EfSLkLT4CUIvMonkAr2R50fFAf4nmilC0M1ZzvB4r"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPublicRsa5(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek =
            "mfZuHzBz0pNDxOi7awB+fDQ/oYkb6RPSEnugmTuhDy0wTXX4b+vdEqK3V9GTboGs8KAbpOUtkLZYWP956lrTMFS/h5A5zzoLCHV"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPublicRsa6(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "vOiW4KkkPaj0tbLqzHMhpeAu2/x/Z2ASQ0/wupGcdqp+6YuO0Q8CwVcZiicpeftAgMBAAE="
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForHmac1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "Hk?H6"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForHmac2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "7>MXa"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForHmac3(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "RD"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForAes1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "yM2Kh&hkGH"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForAes2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "8JKMNR6GTJ"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForAes3(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "KRdFGBvFUI"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForAes4(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "KM"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForIvAes1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "jKL67hJLAr"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForIvAes2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "cT9Hnm"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForClientSecret1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "ChT4D?J"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForClientSecret2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "yiXaD"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }

    private fun generateValueForPrivateRsa1(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "MIIG/AIBADANBgkqhkiG9w0BAQEFAASCBuYwggbiAgEAAoIBgQCpGm9ZJrPDNe3TWzGWq4UfkbLnQXJt3WpfiOBPigXTjZfuBQHG8GLuvKK6gAjVZvFmE6W7cwoPWQIMEknLQ1R/FUwW7rOtmiD5kO+88/1SwO7/tK1VoXuHMXSnVkirDVlko5EGOrd6dO+pLpwOLQCwH8PlpUoXoGAXfdQvFORdeK7k5C/f9jY5mvk/LRxsy/GzCE08tU"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa2(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "Ja3j7jwm4an5IJFg4tgNbq7e+N0yDNdgD1U0MM5ScfLaAj/8FZbKBOECWKVoUK6tQYQ7DEPzBgFeDx0+HBniO4EVwWZEjg5rnWyLnNIc4wJgnsR9IuQtPgJQi8yieQCvZHnR8UB/ieaKULQzVnO8HiuZ9m4fMHPSk0PE6LtrAH58ND+hiRvpE9ISe6CZO6EPLTBNdfhv690SordX0ZNugazwoBuk5S2QtlhY/3nqWtMwVL+HkDnPOgsIdW"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa3(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "86JbgqSQ9qPS1surMcyGl4C7b/H9nYBJDT/C6kZx2qn7pi47RDwLBVxmKJyl5+0CAwEAAQKCAYAxXvRvdOs67T3YXWGm+cDOLL2s4uDDzsdFyTKkRknZMBfReEjCimB4Dz77cHIjzABiqw3SIo4nWPdOCvDclXXP/KnQcDSpVLyX4Ib+BcZKKOYeZePNgm4MVJYYXQquViFpTpAC35W9/PIT1PXe9aijw0Jwg8eUClDDywruDA14dMmrLi"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa4(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "wEGWBPhT2MAmGwKgMRIDVcqhcLbKOz34pB36DFYlkVU+5rfLhaEfAbzH4PIhYLZ8P6SFne6MbMkvhikjfJ1jnyTsFk5juF0qX7Vo6/2pmfum8KDRGOrUa7unD84TTgjhYW7i4ox4+9F8L55Hkop2HkqwK0mZHIaGKNSCxdSrFPWWSUzft8Moyrv8uhg1wbEaxDzCvF959cS8J8U97qZdXV3r8v7TizHuYgk6DjixGHVefOoKVe+U584WPn"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa5(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "eVDtnKddSrXn+l2ZAGvvZDJmVCRkIvUNchkOyDqa6knsRrzNyKRva9l4G9wj1zARn6NEHEpAcXDMILHVzT/GCEECgcEA30Vm5o3PiTAI+M4yOCypCW4k/+DmuT3TjyS65E/JjioYIYKNf/SJ877bBwpklEG4uCim6hlSxyMHgs510qEBM88tcSdXMKF57XOWfm1peqXrq4Fd1yKWNMA9iG0a/OhGPyP57SovN83w1NLKEXUVOSUg+444Mf"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa6(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "5nXk1rn2bq3prSENSp03Bs//5v8G9kKo92wJ7OG+vovqLp3HklLyLHSc9biGH8KIztBY8RIZzCbvm5DZCqc3roBcRUlqbjlo41AoHBAMHkTMZ1NiyOcft4ipXhwGXuUgn1xN6NzjYT/9upJtOuh4fHhA2M+R8Eh6r8mfsqGhWWr0mx2rynSESxUsTJQBDWHskrWZFH2mn20actJRiRQPCq9gOYhYWPkpM3oR33Yr9cuifRzERgji4f1s+a"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa7(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "ppQEWdYBwFvfATVie8+7OKAAECUrQ+K5KOsGxzr3w1J8IJT1nLhwiH48abOPG8d6tnqqbcMM3DkxGhMD5Exu8D03acaDkXiVv2ikd1V/4dmJ2QKBwHrRf6hl6/soXFcZflCeaKISX/IN8n2GACGm79P1/AbjKwEMAvCXQWe+3leCJQ/VE1riqebu5TU8FL6k+fVSsgmg9vtey7yppxuy0N9dKn/YU8q1UDyu/venpHGuqwDiePn0vV/0K6"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa8(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "ND+y3nglaWFIWp4fnfIxSFQLGEXyaBg8POYK33fjAckSlE+qjFbdFnt8h/0xtS62Yq0gCA9651MDFvCof8dqyL0TqUl7TouaA4aM6IfOsnegjqFioFRf6k3QKBwBMmgnLDlvM1qopk8IM3RfKLZGz5alV0+h4bQZUnkt8riNVfXmqcNuraXrADvQ2yrdreBDgqKemiOCAYx40KN5qyG15ROsp/p1H5/+EcM2LGzfw3Vo8qXF3BvX9u6in0"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa9(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "ijtOZFCUhzFlvAVQPb5JjxRljZ32lFI+p3bnYEU3P7OF7hJNwKm/EgyModbwK8mU2v77d1w8OS8GtoX2p46/TxC0jpzKQ2IeuG2SX9Uwy+ev9nGWAyM+3yXtwJ+gPuVmmQKBwFUuimti7i2XAOnS+c/rew6yrpw6XAwWhpyFM8ZBHiEz7RjJIvpLyClHoOoSvBtqoQK5+J3ORsG+s+0qMlTgFlWijWDB20ows+TgLJqzWbDEZMvnJwgcaH"
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }
    private fun generateValueForPrivateRsa10(secretKey: String): HashMap<String, ByteArray> {
        val ubyprislacek = "ZOV/sMEjHBNWhZGBi1SvlBGCNdVohIUXmQQ7yfXo7a5v3P4mLrC3OQxm4PazqFagmp18+BC+HkkBVwatxW83Jm3dfLOyY2mwL4YabGLxPASk6k2iahHUQLDKXljp9ZN7gXPO/A9mMPqA=="
        return encryptValue(
            ubyprislacek.toByteArray(Charsets.UTF_8),
            secretKey
        )
    }


    private fun provideRsaKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_1)
        }
    }

    private fun provideRsaKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_2)
        }
    }

    private fun provideRsaKey3(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_3) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_3)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_3, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_3)
        }
    }

    private fun provideRsaKey4(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_4) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_4)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_4, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_4)
        }
    }

    private fun provideRsaKey5(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_5) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_5)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_5, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_5)
        }
    }

    private fun provideRsaKey6(): String {
        return if (preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_6) != "") {
            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_6)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PUBLIC_RSA_KEY_6, value)

            preferenceHelper.getStringInSharedPreference(PUBLIC_RSA_KEY_6)
        }
    }


    private fun provideHMACKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(HMAC_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(HMAC_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(HMAC_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(HMAC_KEY_1)
        }
    }

    private fun provideHMACKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(HMAC_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(HMAC_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(HMAC_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(HMAC_KEY_2)
        }
    }

    private fun provideHMACKey3(): String {
        return if (preferenceHelper.getStringInSharedPreference(HMAC_KEY_3) != "") {
            preferenceHelper.getStringInSharedPreference(HMAC_KEY_3)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(HMAC_KEY_3, value)

            preferenceHelper.getStringInSharedPreference(HMAC_KEY_3)
        }
    }

    private fun provideAESKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(AES_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(AES_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(AES_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(AES_KEY_1)
        }
    }

    private fun provideAESKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(AES_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(AES_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(AES_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(AES_KEY_2)
        }
    }

    private fun provideAESKey3(): String {
        return if (preferenceHelper.getStringInSharedPreference(AES_KEY_3) != "") {
            preferenceHelper.getStringInSharedPreference(AES_KEY_3)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(AES_KEY_3, value)

            preferenceHelper.getStringInSharedPreference(AES_KEY_3)
        }
    }

    private fun provideAESKey4(): String {
        return if (preferenceHelper.getStringInSharedPreference(AES_KEY_4) != "") {
            preferenceHelper.getStringInSharedPreference(AES_KEY_4)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(AES_KEY_4, value)

            preferenceHelper.getStringInSharedPreference(AES_KEY_4)
        }
    }

    private fun provideIvAESKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(IV_AES_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(IV_AES_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(IV_AES_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(IV_AES_KEY_1)
        }
    }

    private fun provideIvAESKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(IV_AES_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(IV_AES_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(IV_AES_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(IV_AES_KEY_2)
        }
    }


    private fun provideClientSecretKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(CLIENT_SECRET_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_1)
        }
    }


    private fun provideClientSecretKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(CLIENT_SECRET_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(CLIENT_SECRET_KEY_2)
        }
    }

    private fun provideRsaPrivateKey1(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_1) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_1)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_1, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_1)
        }
    }

    private fun provideRsaPrivateKey2(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_2) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_2)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_2, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_2)
        }
    }

    private fun provideRsaPrivateKey3(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_3) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_3)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_3, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_3)
        }
    }

    private fun provideRsaPrivateKey4(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_4) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_4)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_4, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_4)
        }
    }

    private fun provideRsaPrivateKey5(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_5) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_5)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_5, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_5)
        }
    }

    private fun provideRsaPrivateKey6(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_6) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_6)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_6, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_6)
        }
    }

    private fun provideRsaPrivateKey7(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_7) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_7)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_7, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_7)
        }
    }

    private fun provideRsaPrivateKey8(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_8) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_8)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_8, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_8)
        }
    }

    private fun provideRsaPrivateKey9(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_9) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_9)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_9, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_9)
        }
    }

    private fun provideRsaPrivateKey10(): String {
        return if (preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_10) != "") {
            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_10)
        } else {
            val randomValue = UUID.randomUUID().toString()
            val value = getSecretKey(generateSecretKey(randomValue))

            preferenceHelper.saveStringInSharedPreference(PRIVATE_RSA_KEY_10, value)

            preferenceHelper.getStringInSharedPreference(PRIVATE_RSA_KEY_10)
        }
    }


    private fun provideRsaValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa1(provideRsaKey1()),
                secretKey = provideRsaKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey1())
        }
    }

    private fun provideRsaValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa2(provideRsaKey2()),
                secretKey = provideRsaKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey2())
        }
    }

    private fun provideRsaValue3(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey3()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa3(provideRsaKey3()),
                secretKey = provideRsaKey3()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey3(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey3())
        }
    }

    private fun provideRsaValue4(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey4()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa4(provideRsaKey4()),
                secretKey = provideRsaKey4()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey4(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey4())
        }
    }

    private fun provideRsaValue5(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey5()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa5(provideRsaKey5()),
                secretKey = provideRsaKey5()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey5(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey5())
        }
    }

    private fun provideRsaValue6(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaKey6()).ifEmpty {
            val value = getValue(
                value = generateValueForPublicRsa6(provideRsaKey6()),
                secretKey = provideRsaKey6()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaKey6(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaKey6())
        }
    }

    private fun provideHMACValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideHMACKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForHmac1(provideHMACKey1()),
                secretKey = provideHMACKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideHMACKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideHMACKey1())
        }
    }

    private fun provideHMACValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideHMACKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForHmac2(provideHMACKey2()),
                secretKey = provideHMACKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideHMACKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideHMACKey2())
        }
    }

    private fun provideHMACValue3(): String {
        return preferenceHelper.getStringInSharedPreference(provideHMACKey3()).ifEmpty {
            val value = getValue(
                value = generateValueForHmac3(provideHMACKey3()),
                secretKey = provideHMACKey3()
            )
            preferenceHelper.saveStringInSharedPreference(provideHMACKey3(), value)

            preferenceHelper.getStringInSharedPreference(provideHMACKey3())
        }
    }

    private fun provideAESValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideAESKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForAes1(provideAESKey1()),
                secretKey = provideAESKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideAESKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideAESKey1())
        }
    }

    private fun provideAESValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideAESKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForAes2(provideAESKey2()),
                secretKey = provideAESKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideAESKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideAESKey2())
        }
    }

    private fun provideAESValue3(): String {
        return preferenceHelper.getStringInSharedPreference(provideAESKey3()).ifEmpty {
            val value = getValue(
                value = generateValueForAes3(provideAESKey3()),
                secretKey = provideAESKey3()
            )
            preferenceHelper.saveStringInSharedPreference(provideAESKey3(), value)

            preferenceHelper.getStringInSharedPreference(provideAESKey3())
        }
    }

    private fun provideAESValue4(): String {
        return preferenceHelper.getStringInSharedPreference(provideAESKey4()).ifEmpty {
            val value = getValue(
                value = generateValueForAes4(provideAESKey4()),
                secretKey = provideAESKey4()
            )
            preferenceHelper.saveStringInSharedPreference(provideAESKey4(), value)

            preferenceHelper.getStringInSharedPreference(provideAESKey4())
        }
    }

    private fun provideIvAESValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideIvAESKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForIvAes1(provideIvAESKey1()),
                secretKey = provideIvAESKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideIvAESKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideIvAESKey1())
        }
    }

    private fun provideIvAESValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideIvAESKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForIvAes2(provideIvAESKey2()),
                secretKey = provideIvAESKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideIvAESKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideIvAESKey2())
        }
    }

    private fun provideClientSecretValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideClientSecretKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForClientSecret1(provideClientSecretKey1()),
                secretKey = provideClientSecretKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideClientSecretKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideClientSecretKey1())
        }
    }

    private fun provideClientSecretValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideClientSecretKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForClientSecret2(provideClientSecretKey2()),
                secretKey = provideClientSecretKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideClientSecretKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideClientSecretKey2())
        }
    }

    private fun providePrivateRsaValue1(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey1()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa1(provideRsaPrivateKey1()),
                secretKey = provideRsaPrivateKey1()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey1(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey1())
        }
    }

    private fun providePrivateRsaValue2(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey2()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa2(provideRsaPrivateKey2()),
                secretKey = provideRsaPrivateKey2()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey2(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey2())
        }
    }

    private fun providePrivateRsaValue3(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey3()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa3(provideRsaPrivateKey3()),
                secretKey = provideRsaPrivateKey3()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey3(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey3())
        }
    }
    private fun providePrivateRsaValue4(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey4()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa4(provideRsaPrivateKey4()),
                secretKey = provideRsaPrivateKey4()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey4(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey4())
        }
    }
    private fun providePrivateRsaValue5(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey5()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa5(provideRsaPrivateKey5()),
                secretKey = provideRsaPrivateKey5()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey5(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey5())
        }
    }
    private fun providePrivateRsaValue6(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey6()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa6(provideRsaPrivateKey6()),
                secretKey = provideRsaPrivateKey6()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey6(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey6())
        }
    }

    private fun providePrivateRsaValue7(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey7()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa7(provideRsaPrivateKey7()),
                secretKey = provideRsaPrivateKey7()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey7(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey7())
        }
    }

    private fun providePrivateRsaValue8(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey8()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa8(provideRsaPrivateKey8()),
                secretKey = provideRsaPrivateKey8()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey8(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey8())
        }
    }

    private fun providePrivateRsaValue9(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey9()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa9(provideRsaPrivateKey9()),
                secretKey = provideRsaPrivateKey9()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey9(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey9())
        }
    }
    private fun providePrivateRsaValue10(): String {
        return preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey10()).ifEmpty {
            val value = getValue(
                value = generateValueForPrivateRsa10(provideRsaPrivateKey10()),
                secretKey = provideRsaPrivateKey10()
            )
            preferenceHelper.saveStringInSharedPreference(provideRsaPrivateKey10(), value)

            preferenceHelper.getStringInSharedPreference(provideRsaPrivateKey10())
        }
    }

    override fun providePublicRsa(): String {
        return "${provideRsaValue1()}${provideRsaValue2()}${provideRsaValue3()}${provideRsaValue4()}${provideRsaValue5()}${provideRsaValue6()}"
    }

    override fun providePrivateRsa(): String {
        return "${providePrivateRsaValue1()},${providePrivateRsaValue2()},${providePrivateRsaValue3()},${providePrivateRsaValue4()},${providePrivateRsaValue5()},${providePrivateRsaValue6()},${providePrivateRsaValue7()},${providePrivateRsaValue8()},${providePrivateRsaValue9()},${providePrivateRsaValue10()}"
    }

    override fun provideHMAC(): String {
        return "${provideHMACValue1()}${provideHMACValue2()}${provideHMACValue3()}"
    }

    override fun provideAES(): String {
        return "${provideAESValue1()}${provideAESValue2()}${provideAESValue3()}${provideAESValue4()}"
    }

    override fun provideIvAES(): String {
        return "${provideIvAESValue1()}${provideIvAESValue2()}"
    }

    override fun provideClientSecret(): String {
        return "${provideClientSecretValue1()}${provideClientSecretValue2()}"
    }

    override fun provideClientId(): String {
        return "2023"
    }

    override fun provideAuthVersion(): String {
        return "V1.0.0"
    }
}
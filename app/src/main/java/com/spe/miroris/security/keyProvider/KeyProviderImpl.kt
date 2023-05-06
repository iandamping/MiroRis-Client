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

    override fun provideRsa(): String {
        return "${provideRsaValue1()}${provideRsaValue2()}${provideRsaValue3()}${provideRsaValue4()}${provideRsaValue5()}${provideRsaValue6()}"
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
}
package com.spe.miroris.security

interface EncryptionManager {

    fun encryptAes(input: String, aesKey: String, ivKey: String): String

    fun decryptAes(input: String, aesKey: String, ivKey: String): String

    fun encryptRsa(publicKey: String, data: String): String

    fun decryptRsa(privateKey: String, data: String?): String

    fun createHmacSignature(hmacKey: String, value: String): String

    fun signSHA256RSA(input: String, key: String): String

    fun sha256WithBytesToHex(value: String): String

    fun provideRsaPrivateKey(): String

    fun provideClientKey(): String

    fun provideClientSecret(): String

    fun provideClientIV(): String
}
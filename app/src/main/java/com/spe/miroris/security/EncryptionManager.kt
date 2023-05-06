package com.spe.miroris.security

interface EncryptionManager {

    fun encryptAes(input: String): String

    fun decryptAes(input: String): String

    fun encryptRsa(data: String): String

    fun decryptRsa(privateKey: String, data: String?): String

    fun createHmacSignature(value: String): String

    fun signSHA256RSA(input: String, key: String): String

    fun sha256WithBytesToHex(value: String): String

    fun provideHmac512Key(): String

    fun provideRsaPublicKey(): String

    fun provideAesKey(): String

    fun provideAesIVKey(): String

    fun provideClientSecret(): String

    fun provideClientId(): String

    fun provideAuthVersion(): String
}
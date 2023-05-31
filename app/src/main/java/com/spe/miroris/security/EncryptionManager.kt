package com.spe.miroris.security

interface EncryptionManager {

    fun encryptAes(input: String): String

    fun decryptAes(input: String): String

    fun encryptRsa(data: String): String

    fun decryptRsa(data: String): String

    fun createHmacSignature(value: String): String

    fun signSHA256RSA(input: String, key: String): String

    fun sha256WithBytesToHex(value: String): String


}
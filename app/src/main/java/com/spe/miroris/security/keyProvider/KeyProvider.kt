package com.spe.miroris.security.keyProvider

interface KeyProvider {

    fun providePublicRsa(): String

    fun providePrivateRsa():String

    fun provideHMAC(): String

    fun provideAES(): String

    fun provideIvAES(): String

    fun provideClientSecret(): String

    fun provideClientId(): String

    fun provideAuthVersion(): String
}
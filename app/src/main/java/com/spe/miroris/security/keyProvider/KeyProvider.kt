package com.spe.miroris.security.keyProvider

interface KeyProvider {

    fun provideRsa(): String

    fun provideHMAC(): String

    fun provideAES(): String

    fun provideIvAES(): String

    fun provideClientSecret(): String
}
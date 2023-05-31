package com.spe.miroris.security.hmac

import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException

interface HmacManager {

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class
    )
    fun computeHMACSHA512(hmacKey: String, value: String): String

}
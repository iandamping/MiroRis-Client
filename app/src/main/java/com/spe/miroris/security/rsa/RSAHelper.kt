package com.spe.miroris.security.rsa


/**
 * Created by Ian Damping on 26,April,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface RSAHelper {

    fun encrypt(publicKey: String, data: String): String

    fun decrypt(privateKey: String, data: String?): String

}
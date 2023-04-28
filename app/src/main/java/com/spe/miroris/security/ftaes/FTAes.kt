package com.spe.miroris.security.ftaes


/**
 * Created by Ian Damping on 26,April,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface FTAes {

   fun encrypt(input: String, aesKey: String, ivKey: String): String

    fun decrypt(input: String, aesKey: String, ivKey: String): String

}
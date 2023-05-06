package com.spe.miroris

import org.junit.Assert
import org.junit.Test

class ChunkedStringTest {

    @Test
    fun `Test chunked rsa publicKey into list of string`(){
        //given
        val longString = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAqRpvWSazwzXt01sxlquF\n" +
                "H5Gy50Fybd1qX4jgT4oF042X7gUBxvBi7ryiuoAI1WbxZhOlu3MKD1kCDBJJy0NU\n" +
                "fxVMFu6zrZog+ZDvvPP9UsDu/7StVaF7hzF0p1ZIqw1ZZKORBjq3enTvqS6cDi0A\n" +
                "sB/D5aVKF6BgF33ULxTkXXiu5OQv3/Y2OZr5Py0cbMvxswhNPLVCWt4+48JuGp+S\n" +
                "CRYOLYDW6u3vjdMgzXYA9VNDDOUnHy2gI//BWWygThAlilaFCurUGEOwxD8wYBXg\n" +
                "8dPhwZ4juBFcFmRI4Oa51si5zSHOMCYJ7EfSLkLT4CUIvMonkAr2R50fFAf4nmil\n" +
                "C0M1ZzvB4rmfZuHzBz0pNDxOi7awB+fDQ/oYkb6RPSEnugmTuhDy0wTXX4b+vdEq\n" +
                "K3V9GTboGs8KAbpOUtkLZYWP956lrTMFS/h5A5zzoLCHVvOiW4KkkPaj0tbLqzHM\n" +
                "hpeAu2/x/Z2ASQ0/wupGcdqp+6YuO0Q8CwVcZiicpeftAgMBAAE=\n"
        //when
        val chunkedString = longString.chunked(100)
        Assert.assertNotEquals(emptyList<String>(),chunkedString)
    }


    @Test
    fun `Test chunked hmac into list of string`(){
        //given
        val longString = "Hk?H67>MXaRD"
        //when
        val chunkedString = longString.chunked(5)
        Assert.assertNotEquals(emptyList<String>(),chunkedString)
    }

    @Test
    fun `Test chunked aes into list of string`(){
        //given
        val longString = "yM2Kh&hkGH8JKMNR6GTJKRdFGBvFUIKM"
        //when
        val chunkedString = longString.chunked(10)
        Assert.assertNotEquals(emptyList<String>(),chunkedString)
    }

    @Test
    fun `Test chunked ivAes into list of string`(){
        //given
        val longString = "jKL67hJLArcT9Hnm"
        //when
        val chunkedString = longString.chunked(10)
        Assert.assertNotEquals(emptyList<String>(),chunkedString)
    }

    @Test
    fun `Test chunked clientSecret into list of string`(){
        //given
        val longString = "ChT4D?JyiXaD"
        //when
        val chunkedString = longString.chunked(7)
        Assert.assertNotEquals(emptyList<String>(),chunkedString)
    }
}
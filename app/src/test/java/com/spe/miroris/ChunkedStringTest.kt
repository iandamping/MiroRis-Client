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
        Assert.assertNotEquals(emptyList<String>(), chunkedString)
    }

    @Test
    fun `Test chunked rsa privateKey into list of string`() {
        //given
        val longString =
            "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"
        //when
        val chunkedString = longString.chunked(250)
        Assert.assertEquals(emptyList<String>(), chunkedString)
        val split = listOf<String>(
            "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"
        )
    }


    @Test
    fun `Test chunked hmac into list of string`() {
        //given
        val longString = "Hk?H67>MXaRD"
        //when
        val chunkedString = longString.chunked(5)
        Assert.assertNotEquals(emptyList<String>(), chunkedString)
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
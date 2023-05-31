package com.spe.miroris

import com.spe.miroris.core.data.dataSource.remote.model.response.DecryptedErrorResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultDecryptedErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test

class MoshiParserTest {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

    @Test
    fun `Test error message`() {
        val errorMessage =
            "{\"code\":300,\"message_id\":\"gagal\",\"message_en\":\"failed\",\"data\":[{\"field\":\"password\",\"message\":\"Incorrect email or password.\"}]}"

        val types =
            Types.newParameterizedType(
                DefaultDecryptedErrorBaseResponse::class.java,
                DecryptedErrorResponse::class.java
            )
        val data =
            moshiParser.fromJson<DefaultDecryptedErrorBaseResponse<DecryptedErrorResponse>>(
                errorMessage,
                types
            )




        Assert.assertEquals(300, data?.code)
        Assert.assertEquals("Incorrect email or password.", data?.data?.first()?.message)

    }

    @Test
    fun `Test default error message`() {
        val errorMessage =
            "{\"code\":0,\"status\":404,\"name\":\"Not Found\",\"message\":\"Page not found.\"}"

        val types = Types.newParameterizedType(DefaultErrorBaseResponse::class.java,DefaultErrorBaseResponse::class.java)
        val data = moshiParser.fromJson<DefaultErrorBaseResponse>(errorMessage, types)




        Assert.assertEquals(0, data?.code)
        Assert.assertEquals("Page not found.", data?.message)

    }
}
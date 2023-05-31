package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.RefreshTokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class EncryptedRemoteRefreshTokenDataSourceImplTest {

    private lateinit var sut: EncryptedRemoteRefreshTokenDataSource
    private val apiInterface: EncryptedApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

    @Before
    fun setUp() {
        sut = EncryptedRemoteRefreshTokenDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            moshiParser,
            encryptionManager
        )
        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
        every { encryptionManager.decryptRsa(any()) } returns "deencrypted rsa"
    }

    @Test
    fun `when datasource call refreshToken should return Success`() = runTest {
        //given
        val tokenResponse = RefreshTokenResponse("deencrypted rsa", 3600)
        val successResponse = RefreshTokenRemoteResult.Success(tokenResponse)
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 200,\n" +
                "    \"message_en\": \"Success\",\n" +
                "    \"message_id\": \"Sukses\",\n" +
                "    \"data\": {\n" +
                "        \"token\":\"a\",\n" +
                "        \"expired\": 3600\n" +
                "    }\n" +
                "}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success(
                "random string"
            )
        )

        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call refreshToken should return Error from code 201`() = runTest {
        //given
        val successResponse = RefreshTokenRemoteResult.Error("Failed")
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 201,\n" +
                "    \"message_en\": \"Failed\",\n" +
                "    \"message_id\": \"Gagal\",\n" +
                "    \"data\": {\n" +
                "        \"token\":\"a\",\n" +
                "        \"expired\": 3600\n" +
                "    }\n" +
                "}"

        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success(
                "random string"
            )
        )

        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getToken should return Error from remoteHelper`() = runTest {
        //given
        val errorResponse = RefreshTokenRemoteResult.Error("code : 300 error : Incorrect email or password.")
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":300,\"message_id\":\"gagal\",\"message_en\":\"failed\",\"data\":[{\"field\":\"password\",\"message\":\"Incorrect email or password.\"}]}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Error(
            Exception("Failed")
        )
        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call refreshToken should return Error from null body`() = runTest {
        //given
        val successResponse = RefreshTokenRemoteResult.Error("response body is null")
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success(null)
        )

        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call refreshToken should return Error from null data`() = runTest {
        //given
        val successResponse = RefreshTokenRemoteResult.Error("data from service is null")
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 200,\n" +
                "    \"message_en\": \"Success\",\n" +
                "    \"message_id\": \"Sukses\",\n" +
                "    \"data\": null \n" +
                "}"

        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success(
                "random string"
            )
        )

        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call refreshToken should return Error from remoteHelper`() = runTest {
        //given
        val successResponse = RefreshTokenRemoteResult.Error("Error")
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Error(
            Exception("Error")
        )
        //when
        val actualResult = sut.refreshToken("c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.refreshToken(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call refreshToken should return Error from catching exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.refreshToken(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult = sut.refreshToken("c", "c")

            coVerify(exactly = 1, verifyBlock = { apiInterface.refreshToken(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                "Error", (actualResult as RefreshTokenRemoteResult.Error).errorMessage
            )
        }

}
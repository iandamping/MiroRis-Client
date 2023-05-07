package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
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

class EncryptedRemoteTokenDataSourceImplTest {

    private lateinit var sut: EncryptedRemoteTokenDataSource
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
        sut = EncryptedRemoteTokenDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            moshiParser,
            encryptionManager
        )
        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
        every { encryptionManager.provideAuthVersion() } returns "auth"
        every { encryptionManager.provideClientId() } returns "id"
        every { encryptionManager.provideClientSecret() } returns "secret"
    }


    @Test
    fun `when datasource call getToken should return Success`() = runTest {
        //given
        val tokenResponse = TokenResponse("a", "Bearer", "3600")
        val successResponse = TokenRemoteResult.Success(tokenResponse)
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 200,\n" +
                        "    \"message_en\": \"Success\",\n" +
                        "    \"message_id\": \"Sukses\",\n" +
                        "    \"data\": {\n" +
                        "        \"access_token\":\"a\",\n" +
                        "        \"type\": \"Bearer\",\n" +
                        "        \"expired\": 3600\n" +
                        "    }\n" +
                        "}"
            )
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from code 201`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("Failed")
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 201,\n" +
                        "    \"message_en\": \"Failed\",\n" +
                        "    \"message_id\": \"Gagal\",\n" +
                        "    \"data\": {\n" +
                        "        \"access_token\":\"a\",\n" +
                        "        \"type\": \"Bearer\",\n" +
                        "        \"expired\": 3600\n" +
                        "    }\n" +
                        "}"
            )
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from null body`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("response body is null")
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(null)
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getToken should return Error from null data`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("data from service is null")
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 200,\n" +
                        "    \"message_en\": \"Success\",\n" +
                        "    \"message_id\": \"Sukses\",\n" +
                        "    \"data\": null \n" +
                        "}"
            )
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from remoteHelper`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("Failed")
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.Error(
            Exception("Failed")
        )
        //when
        val actualResult = sut.getToken("c", "c", "c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getToken should return Error from EncryptionError`() =
        runTest {
            //given
            coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) } returns RemoteEncryptedResult.EncryptionError
            //when
            val actualResult = sut.getToken("c", "c", "c", "c","c")
            coVerify(
                exactly = 1,
                verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any(),any())) })
            coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any(),any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                TokenRemoteResult.EncryptionError, actualResult
            )
        }
}
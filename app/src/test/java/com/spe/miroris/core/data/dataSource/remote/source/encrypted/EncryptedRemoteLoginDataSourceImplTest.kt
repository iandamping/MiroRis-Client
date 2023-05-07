package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
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

class EncryptedRemoteLoginDataSourceImplTest {

    private lateinit var sut: EncryptedRemoteLoginDataSource
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
        sut = EncryptedRemoteLoginDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            moshiParser,
            encryptionManager
        )
        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }


    @Test
    fun `when datasource call userLogin should return Success`() = runTest {
        //given
        val loginResponse = LoginResponse("token")
        val successResponse = LoginRemoteResult.Success(loginResponse)
        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 200,\n" +
                        "    \"message_en\": \"Success\",\n" +
                        "    \"message_id\": \"Sukses\",\n" +
                        "    \"data\": {\n" +
                        "        \"token\":    \"token\"\n" +
                        "    },\n" +
                        " \"token\": 3600\n" +
                        "}\n"
            )
        )

        //when
        val actualResult = sut.userLogin("c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call userLogin should return Error from code 201`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("Failed")
        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 201,\n" +
                        "    \"message_en\": \"Failed\",\n" +
                        "    \"message_id\": \"Gagal\",\n" +
                        "    \"data\": {\n" +
                        "        \"token\":    \"token\"\n" +
                        "    },\n" +
                        " \"token\": 3600\n" +
                        "}\n"
            )
        )

        //when
        val actualResult = sut.userLogin("c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call userLogin should return Error from null body`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("response body is null")
        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(null)
        )

        //when
        val actualResult = sut.userLogin("c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call userLogin should return Error from null data`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("data from service is null")
        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(
                "{\n" +
                        "    \"code\": 200,\n" +
                        "    \"message_en\": \"Success\",\n" +
                        "    \"message_id\": \"Sukses\",\n" +
                        "    \"data\": null,\n" +
                        " \"token\": 3600\n" +
                        "}\n"
            )
        )

        //when
        val actualResult = sut.userLogin("c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call userLogin should return Error from remoteHelper`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("Failed")
        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Error(
            Exception("Failed")
        )
        //when
        val actualResult = sut.userLogin("c", "c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call userLogin should return Error from EncryptionError`() =
        runTest {
            //given
            coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.EncryptionError
            //when
            val actualResult = sut.userLogin("c", "c","c")
            coVerify(
                exactly = 1,
                verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
            coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                LoginRemoteResult.EncryptionError, actualResult
            )
        }

}
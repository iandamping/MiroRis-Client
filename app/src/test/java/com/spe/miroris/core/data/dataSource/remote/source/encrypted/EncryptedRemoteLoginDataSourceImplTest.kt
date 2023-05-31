package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
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
import java.net.SocketTimeoutException

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
        every { encryptionManager.decryptRsa(any()) } returns "deencrypted rsa"
    }


    @Test
    fun `when datasource call userLogin should return Success`() = runTest {
        //given
        val loginResponse = LoginResponse("deencrypted rsa", 3600, "Bearer")
        val successResponse = LoginRemoteResult.Success(loginResponse)
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 200,\n" +
                "    \"message_en\": \"Success\",\n" +
                "    \"message_id\": \"Sukses\",\n" +
                "    \"data\": {\n" +
                "        \"token\":\"a\",\n" +
                "        \"email\": \"Bearer\",\n" +
                "        \"expired\": 3600\n" +
                "    }\n" +
                "}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.userLogin(
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
        val actualResult = sut.userLogin("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.userLogin(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.decryptAes(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call userLogin should return Error from code 201`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("Failed")
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 201,\n" +
                "    \"message_en\": \"Failed\",\n" +
                "    \"message_id\": \"Gagal\",\n" +
                "    \"data\": {\n" +
                "        \"token\":\"a\",\n" +
                "        \"email\": \"Bearer\",\n" +
                "        \"expired\": 3600\n" +
                "    }\n" +
                "}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.userLogin(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success("random string")
        )

        //when
        val actualResult = sut.userLogin("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.encryptionCall(
                    apiInterface.userLogin(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.decryptAes(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
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
        val actualResult = sut.userLogin("c", "c","c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call userLogin should return Error from null data`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("data from service is null")
        every { encryptionManager.decryptAes(any()) } returns "{\n" +
                "    \"code\": 200,\n" +
                "    \"message_en\": \"Success\",\n" +
                "    \"message_id\": \"Sukses\",\n" +
                "    \"data\": null,\n" +
                " \"token\": 3600\n" +
                "}\n"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.userLogin(
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
        val actualResult = sut.userLogin("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.decryptAes(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call userLogin should return Error from remoteHelper`() = runTest {
        //given
        val errorResponse = LoginRemoteResult.Error("code : 300 error : Incorrect email or password.")
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":300,\"message_id\":\"gagal\",\"message_en\":\"failed\",\"data\":[{\"field\":\"password\",\"message\":\"Incorrect email or password.\"}]}"

        coEvery { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) } returns RemoteEncryptedResult.Error(
            Exception("Failed")
        )
        //when
        val actualResult = sut.userLogin("c", "c","c","c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.userLogin(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
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
            val actualResult = sut.userLogin("c", "c","c","c")
            coVerify(
                exactly = 1,
                verifyBlock = {
                    remoteHelper.encryptionCall(
                        apiInterface.userLogin(
                            any(),
                            any(),
                            any()
                        )
                    )
                })
            coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                LoginRemoteResult.EncryptionError, actualResult
            )
        }

    @Test
    fun `when datasource call userLogin should return Error from catching Exception`() =
        runTest {
            //given
            coEvery { apiInterface.userLogin(any(), any(), any()) } throws SocketTimeoutException()
            //when
            val actualResult = sut.userLogin("c", "c", "c", "c")

            coVerify(exactly = 1, verifyBlock = { apiInterface.userLogin(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                "Error", (actualResult as LoginRemoteResult.Error).errorMessage
            )
        }

}
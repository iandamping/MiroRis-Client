package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LogoutRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class RemoteLogoutDataSourceImplTest {

    private lateinit var sut: RemoteLogoutDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

    @Before
    fun setUp() {
        sut = RemoteLogoutDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            encryptionManager,
            moshiParser
        )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }

    @Test
    fun `when datasource call logoutUser should return Success`() = runTest {
        //given
        val successResponse = LogoutRemoteResult.Success
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.logoutUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success(
            Response.success(
                VoidBaseResponse(
                    200,
                    "Success",
                    "Sukses"
                )
            )
        )
        //when
        val actualResult = sut.logoutUser("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.logoutUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.logoutUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call logoutUser should return Error`() = runTest {
        //given
        val successResponse = LogoutRemoteResult.Error("Your request was made with invalid credentials.")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.logoutUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Error(Exception("{\"code\":0,\"status\":401,\"name\":\"Unauthorized\",\"message\":\"Your request was made with invalid credentials.\"}"))
        //when
        val actualResult = sut.logoutUser("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.logoutUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.logoutUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call logoutUser should return Error from code 201`() = runTest {
        //given
        val successResponse = LogoutRemoteResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.logoutUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success(
            Response.success(
                VoidBaseResponse(
                    201,
                    "Failed",
                    "Gagal"
                )
            )
        )
        //when
        val actualResult = sut.logoutUser("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.logoutUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.logoutUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call logoutUser should return Error from null body`() = runTest {
        //given
        val successResponse = LogoutRemoteResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.logoutUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.logoutUser("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.logoutUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.logoutUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call logoutUser should return Error from catching exception`() = runTest {
        //given
        coEvery {
            apiInterface.logoutUser(
                any(),
                any(),
                any()
            )
        } throws SocketTimeoutException()
        //when
        val actualResult = sut.logoutUser("a", "a", "a")

        coVerify(exactly = 1, verifyBlock = { apiInterface.logoutUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            "Error", (actualResult as LogoutRemoteResult.Error).errorMessage
        )
    }

}
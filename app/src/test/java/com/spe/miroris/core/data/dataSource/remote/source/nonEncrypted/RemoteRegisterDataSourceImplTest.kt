package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.RegisterErrorResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
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

class RemoteRegisterDataSourceImplTest {

    private lateinit var sut: RemoteRegisterDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut = RemoteRegisterDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            encryptionManager
        )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }

    @Test
    fun `when datasource call registerUser should return Success`() = runTest {
        //given
        val successResponse = RegisterRemoteResult.Success
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    200,
                    "Success",
                    "Sukses",
                    null
                )
            )
        )
        //when
        val actualResult = sut.registerUser("a","a","a","a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }
    @Test
    fun `when datasource call registerUser should return Error`() = runTest {
        //given
        val successResponse = RegisterRemoteResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Error(Exception("Failed"))
        //when
        val actualResult = sut.registerUser("a","a","a","a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call registerUser should return Error from 201 code`() = runTest {
        //given
        val successResponse =
            RegisterRemoteResult.Error("code : 201 error : Email is not a valid email address. & Passwords don't match")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    201,
                    "Failed",
                    "Gagal",
                    RegisterErrorResponse(
                        listOf("Email is not a valid email address."),
                        listOf("Passwords don't match")
                    )
                )
            )
        )
        //when
        val actualResult = sut.registerUser("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call registerUser should return Error from 201 code with null data`() =
        runTest {
            //given
            val successResponse = RegisterRemoteResult.Error("code : 201 error : Failed")
            coEvery {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            } returns RemoteResult.Success(
                Response.success(
                    BaseResponse(
                        201,
                        "Failed",
                        "Gagal",
                        null
                    )
                )
            )
        //when
        val actualResult = sut.registerUser("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call registerUser should return Error from null body`() = runTest {
        //given
        val successResponse = RegisterRemoteResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                null
            )
        )
        //when
        val actualResult = sut.registerUser("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.registerUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call registerUser should return Error from catching exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult = sut.registerUser("a", "a", "a", "a")

            coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                "Error", (actualResult as RegisterRemoteResult.Error).errorMessage
            )
        }

}
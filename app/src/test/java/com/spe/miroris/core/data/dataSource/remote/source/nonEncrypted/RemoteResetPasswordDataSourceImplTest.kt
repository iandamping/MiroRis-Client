package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ResetPasswordResult
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
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

class RemoteResetPasswordDataSourceImplTest {

    private lateinit var sut: RemoteResetPasswordDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut = RemoteResetPasswordDataSourceImpl(
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
    fun `when datasource call resetPassword should return Success`() = runTest {
        //given
        val successResponse = ResetPasswordResult.Success
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.resetPassword(
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
        val actualResult = sut.resetPassword("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.resetPassword(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.resetPassword(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call resetPassword should return Error`() = runTest {
        //given
        val successResponse = ResetPasswordResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.resetPassword(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Error(Exception("Failed"))
        //when
        val actualResult = sut.resetPassword("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.resetPassword(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.resetPassword(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call resetPassword should return Error from code 201`() = runTest {
        //given
        val successResponse = ResetPasswordResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.resetPassword(
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
        val actualResult = sut.resetPassword("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.resetPassword(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.resetPassword(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call resetPassword should return Error from null body`() = runTest {
        //given
        val successResponse = ResetPasswordResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.resetPassword(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.resetPassword("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.resetPassword(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.resetPassword(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call resetPassword should return Error from catching exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.resetPassword(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult = sut.resetPassword("a", "a", "a", "a")

            coVerify(exactly = 1, verifyBlock = { apiInterface.resetPassword(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                "Error", (actualResult as ResetPasswordResult.Error).errorMessage
            )
        }


}
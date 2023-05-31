package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class RemoteDeactivateProductDataSourceImplTest {

    private lateinit var sut: RemoteDeactivateProductDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut = RemoteDeactivateProductDataSourceImpl(
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
    fun `when datasource call deactivateProduct should return Success`() = runTest {
        //given
        val successResponse = DeactivateProductResult.Success
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.deactivateProduct(
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
        val actualResult =
            sut.deactivateProduct("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.deactivateProduct(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.deactivateProduct(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call deactivateProduct should return Error from code 201`() = runTest {
        //given
        val successResponse = DeactivateProductResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.deactivateProduct(
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
        val actualResult =
            sut.deactivateProduct("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.deactivateProduct(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.deactivateProduct(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call deactivateProduct should return Error from null body`() = runTest {
        //given
        val successResponse = DeactivateProductResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.deactivateProduct(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success(
            Response.success(
                null
            )
        )
        //when
        val actualResult =
            sut.deactivateProduct("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionVoidCall(
                    apiInterface.deactivateProduct(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.deactivateProduct(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call deactivateProduct should return Error from catching exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.deactivateProduct(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult =
                sut.deactivateProduct("a", "a", "a")

            coVerify(
                exactly = 1,
                verifyBlock = { apiInterface.deactivateProduct(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            assertEquals(
                "Error", (actualResult as DeactivateProductResult.Error).errorMessage
            )
        }
}
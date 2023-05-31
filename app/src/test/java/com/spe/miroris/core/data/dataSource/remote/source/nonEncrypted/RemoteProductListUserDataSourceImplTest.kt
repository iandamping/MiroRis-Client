package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductListUserResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse
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

class RemoteProductListUserDataSourceImplTest {

    private lateinit var sut: RemoteProductListUserDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut =
            RemoteProductListUserDataSourceImpl(
                apiInterface,
                remoteHelper,
                utilityHelper,
                encryptionManager
            )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }

    @Test
    fun `when datasource call getProductListUser should return Success`() = runTest {
        //given
        val productResponse =
            ProductListUserResponse(
                listOf(
                    ProductUserResponse(
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                        "a",
                    )
                ), 1
            )
        val successResponse = ProductListUserResult.Success(productResponse)
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getProductListUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 200,
                    messageEnglish = "Success",
                    messageIndonesia = "Sukses",
                    data = productResponse
                )
            )
        )
        //when
        val actualResult = sut.getProductList("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getProductListUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(
            exactly = 1,
            verifyBlock = { apiInterface.getProductListUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductListUser should return Error from code 201`() = runTest {
        //given
        val errorResponse = ProductListUserResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getProductListUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 201,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagal",
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getProductList("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getProductListUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(
            exactly = 1,
            verifyBlock = { apiInterface.getProductListUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductListUser should return Error from null data`() = runTest {
        //given
        val errorResponse = ProductListUserResult.Error("data from service is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getProductListUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 200,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagal",
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getProductList("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getProductListUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(
            exactly = 1,
            verifyBlock = { apiInterface.getProductListUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductListUser should return Error from null body`() = runTest {
        //given
        val errorResponse = ProductListUserResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getProductListUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.getProductList("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getProductListUser(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(
            exactly = 1,
            verifyBlock = { apiInterface.getProductListUser(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductListUser should return Error from throws exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.getProductListUser(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult = sut.getProductList("a", "a", "a")

            coVerify(
                exactly = 1,
                verifyBlock = { apiInterface.getProductListUser(any(), any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            //then
            assertEquals(
                "Error", (actualResult as ProductListUserResult.Error).errorMessage
            )
        }

}
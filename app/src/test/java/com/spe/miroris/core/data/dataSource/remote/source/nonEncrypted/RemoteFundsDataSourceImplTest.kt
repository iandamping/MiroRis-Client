package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FundsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.FundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
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

class RemoteFundsDataSourceImplTest {
    private lateinit var sut: RemoteFundsDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut =
            RemoteFundsDataSourceImpl(
                apiInterface,
                remoteHelper,
                utilityHelper,
                encryptionManager
            )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
    }

    @Test
    fun `when datasource call getFunds should return Success`() = runTest {
        //given
        val fundsResponse =
            ListFundResponse(
                listOfFund = listOf(FundResponse("a", "a", "a", "a", "a", "a", "a")),
                "a",
                "a",
                2,
                1
            )
        val successResponse = FundsResult.Success(fundsResponse)
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getFunds(
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
                    data = fundsResponse
                )
            )
        )
        //when
        val actualResult = sut.getFunds("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getFunds(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getFunds(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getFunds should return Error from code 201`() = runTest {
        //given
        val fundsResponse =
            ListFundResponse(
                listOfFund = listOf(FundResponse("a", "a", "a", "a", "a", "a", "a")),
                "a",
                "a",
                2,
                1
            )
        val successResponse = FundsResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getFunds(
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
                    data = fundsResponse
                )
            )
        )
        //when
        val actualResult = sut.getFunds("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getFunds(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getFunds(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getFunds should return Error from null data`() = runTest {
        //given

        val successResponse = FundsResult.Error("data from service is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getFunds(
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
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getFunds("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getFunds(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getFunds(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getFunds should return Error from null body`() = runTest {
        //given

        val successResponse = FundsResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getFunds(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.getFunds("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getFunds(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getFunds(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getFunds should return Error from catching exception`() = runTest {
        //given
        coEvery {
            apiInterface.getFunds(
                any(),
                any(),
                any()
            )
        } throws SocketTimeoutException()
        //when
        val actualResult = sut.getFunds("a", "a", "a")

        coVerify(exactly = 1, verifyBlock = { apiInterface.getFunds(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            "Error", (actualResult as FundsResult.Error).errorMessage
        )
    }
}
package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.InvestmentResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
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

class RemoteInvestmentsDataSourceImplTest {

    private lateinit var sut: RemoteInvestmentsDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut =
            RemoteInvestmentsDataSourceImpl(
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
    fun `when datasource call getInvestments should return Success`() = runTest {
        //given
        val investmentsResponse =
            ListInvestmentResponse(
                listOfInvestment = listOf(InvestmentResponse("a", "a", "a", "a", "a", "a")),
                "a",
                1,
                "20000",
                1
            )
        val successResponse = InvestmentsResult.Success(investmentsResponse)
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getInvestments(
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
                    data = investmentsResponse
                )
            )
        )
        //when
        val actualResult = sut.getInvestments("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getInvestments(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getInvestments(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getInvestments should return Error from code 201`() = runTest {
        //given
        val investmentsResponse =
            ListInvestmentResponse(
                listOfInvestment = listOf(InvestmentResponse("a", "a", "a", "a", "a", "a")),
                "a",
                1,
                "20000",
                1
            )
        val successResponse = InvestmentsResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getInvestments(
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
                    data = investmentsResponse
                )
            )
        )
        //when
        val actualResult = sut.getInvestments("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getInvestments(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getInvestments(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getInvestments should return Error from null data`() = runTest {
        //given

        val successResponse = InvestmentsResult.Error("data from service is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getInvestments(
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
        val actualResult = sut.getInvestments("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getInvestments(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getInvestments(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getInvestments should return Error from null body`() = runTest {
        //given

        val successResponse = InvestmentsResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getInvestments(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.getInvestments("a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getInvestments(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getInvestments(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getInvestments should return Error from catching exception`() = runTest {
        //given

        coEvery {
            apiInterface.getInvestments(
                any(),
                any(),
                any()
            )
        } throws SocketTimeoutException()
        //when
        val actualResult = sut.getInvestments("a", "a", "a")

        coVerify(exactly = 1, verifyBlock = { apiInterface.getInvestments(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            "Error", (actualResult as InvestmentsResult.Error).errorMessage
        )
    }
}
package com.spe.miroris.core.data.repository

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DetailFundResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FundsResult
import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.FundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.InvestmentResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.repository.InvestmentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FundRepositoryImplTest {

    private lateinit var sut: InvestmentRepository

    private val remoteDataSource: RemoteDataSource = mockk()
    private val cacheDataSource: CacheDataSource = mockk()

    @Before
    fun setUp() {
        sut = InvestmentRepositoryImpl(remoteDataSource, cacheDataSource)
        every { cacheDataSource.getUserToken() } returns "token"
        every { cacheDataSource.getUserBearer() } returns "bearer"
    }

    @Test
    fun `When repository invoke getBank return Success`() = runTest {
        //given
        val bankResponse = ListBankResponse(listOf(BankResponse("a", "a", "a")), 1)
        coEvery {
            remoteDataSource.getBank(
                any(),
                any(),
                any(),
                any()
            )
        } returns BanksResult.Success(bankResponse)
        //when
        val result = sut.getBank("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getBank(any(), any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Success(bankResponse), result
        )
    }

    @Test
    fun `When repository invoke getBank return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.getBank(
                any(),
                any(),
                any(),
                any()
            )
        } returns BanksResult.Error("failed")
        //when
        val result = sut.getBank("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getBank(any(), any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Error("failed"), result
        )
    }


    @Test
    fun `When repository invoke getFunds return Success`() = runTest {
        //given
        val fundResponse = ListFundResponse(
            listOf(FundResponse("a", "a", "a", "a", "a", "a", "a")),
            "a",
            "a",
            1,
            1
        )
        coEvery {
            remoteDataSource.getFunds(
                any(),
                any(),
                any(),
            )
        } returns FundsResult.Success(fundResponse)
        //when
        val result = sut.getFunds("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getFunds(any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Success(fundResponse), result
        )
    }


    @Test
    fun `When repository invoke getFunds return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.getFunds(
                any(),
                any(),
                any(),
            )
        } returns FundsResult.Error("failed")
        //when
        val result = sut.getFunds("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getFunds(any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Error("failed"), result
        )
    }


    @Test
    fun `When repository invoke getInvestments return Success`() = runTest {
        //given
        val investmentResponse = ListInvestmentResponse(
            listOf(InvestmentResponse("a", "a", "a", "a", "a", "a")),
            "a",
            1,
            "a",
            1
        )
        coEvery {
            remoteDataSource.getInvestments(
                any(),
                any(),
                any(),
            )
        } returns InvestmentsResult.Success(investmentResponse)
        //when
        val result = sut.getInvestments("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getInvestments(any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Success(investmentResponse), result
        )
    }

    @Test
    fun `When repository invoke getInvestments return Error`() = runTest {
        //given

        coEvery {
            remoteDataSource.getInvestments(
                any(),
                any(),
                any(),
            )
        } returns InvestmentsResult.Error("failed")
        //when
        val result = sut.getInvestments("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getInvestments(any(), any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Error("failed"), result
        )
    }


    @Test
    fun `When repository invoke detailFund return Success`() = runTest {
        //given
        val fundResponse =
            DetailFundResponse("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
        coEvery {
            remoteDataSource.detailFund(
                any(),
                any(),
            )
        } returns DetailFundResult.Success(fundResponse)
        //when
        val result = sut.detailFund("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.detailFund(any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Success(fundResponse), result
        )
    }


    @Test
    fun `When repository invoke detailFund return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.detailFund(
                any(),
                any(),
            )
        } returns DetailFundResult.Error("fundResponse")
        //when
        val result = sut.detailFund("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.detailFund(any(), any()) })

        Assert.assertEquals(
            DomainInvestmentResult.Error("fundResponse"), result
        )
    }


}
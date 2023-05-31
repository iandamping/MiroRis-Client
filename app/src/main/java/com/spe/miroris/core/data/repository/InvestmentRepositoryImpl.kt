package com.spe.miroris.core.data.repository

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DetailFundResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FundsResult
import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.repository.InvestmentRepository
import javax.inject.Inject

class InvestmentRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cacheDataSource: CacheDataSource
) : InvestmentRepository {
    override suspend fun getBank(
        page: String,
        limit: String,
        bankName: String
    ): DomainInvestmentResult<ListBankResponse> {
        return when (val remoteData = remoteDataSource.getBank(
            page = page,
            limit = limit,
            bankName = bankName,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is BanksResult.Error -> DomainInvestmentResult.Error(remoteData.errorMessage)
            is BanksResult.Success -> {
                DomainInvestmentResult.Success(remoteData.data)
            }

            BanksResult.RefreshToken -> DomainInvestmentResult.RefreshToken
        }
    }

    override suspend fun getFunds(
        page: String,
        limit: String
    ): DomainInvestmentResult<ListFundResponse> {
        return when (val remoteData = remoteDataSource.getFunds(
            page = page,
            limit = limit,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is FundsResult.Error -> DomainInvestmentResult.Error(remoteData.errorMessage)
            is FundsResult.Success -> DomainInvestmentResult.Success(remoteData.data)
            FundsResult.RefreshToken -> DomainInvestmentResult.RefreshToken
        }
    }

    override suspend fun getInvestments(
        page: String,
        limit: String
    ): DomainInvestmentResult<ListInvestmentResponse> {
        return when (val remoteData = remoteDataSource.getInvestments(
            page = page,
            limit = limit,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is InvestmentsResult.Error -> DomainInvestmentResult.Error(remoteData.errorMessage)
            is InvestmentsResult.Success -> DomainInvestmentResult.Success(remoteData.data)
            InvestmentsResult.RefreshToken -> DomainInvestmentResult.RefreshToken
        }
    }

    override suspend fun detailFund(id: String): DomainInvestmentResult<DetailFundResponse> {
        return when (val remoteData = remoteDataSource.detailFund(
            id = id,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is DetailFundResult.Error -> DomainInvestmentResult.Error(remoteData.errorMessage)
            is DetailFundResult.Success -> DomainInvestmentResult.Success(remoteData.data)
        }
    }
}
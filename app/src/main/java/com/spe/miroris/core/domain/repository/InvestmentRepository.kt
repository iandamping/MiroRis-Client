package com.spe.miroris.core.domain.repository

import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.domain.common.DomainInvestmentResult

interface InvestmentRepository {

    suspend fun getBank(
        page: String,
        limit: String,
        bankName: String,
    ): DomainInvestmentResult<ListBankResponse>


    suspend fun getFunds(
        page: String,
        limit: String,
    ): DomainInvestmentResult<ListFundResponse>


    suspend fun getInvestments(
        page: String,
        limit: String,
    ): DomainInvestmentResult<ListInvestmentResponse>

    suspend fun detailFund(id: String): DomainInvestmentResult<DetailFundResponse>
}
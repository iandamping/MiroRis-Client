package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse


interface RemoteInvestmentsDataSource {

    suspend fun getInvestments(
        page: String,
        limit: String,
        token: String
    ): InvestmentsResult<ListInvestmentResponse>
}
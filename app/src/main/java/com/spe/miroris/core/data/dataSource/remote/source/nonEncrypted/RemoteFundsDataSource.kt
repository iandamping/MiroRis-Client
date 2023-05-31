package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.FundsResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse


interface RemoteFundsDataSource {

    suspend fun getFunds(page:String, limit:String, token:String) : FundsResult<ListFundResponse>

}
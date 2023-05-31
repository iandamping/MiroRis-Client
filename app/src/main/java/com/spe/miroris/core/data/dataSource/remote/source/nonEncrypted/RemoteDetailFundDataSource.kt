package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.DetailFundResult
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse


interface RemoteDetailFundDataSource {

    suspend fun detailFund(id: String, token: String): DetailFundResult<DetailFundResponse>

}
package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse


interface RemoteBanksDataSource {

    suspend fun getBank(page: String, limit: String, bankName: String, token: String):BanksResult<ListBankResponse>
}
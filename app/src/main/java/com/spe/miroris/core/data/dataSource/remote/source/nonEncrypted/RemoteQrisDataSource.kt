package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.GenerateQrResult
import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse

interface RemoteQrisDataSource {

    suspend fun generateQr(productId: String, token:String): GenerateQrResult<GenerateQrResponse>

}
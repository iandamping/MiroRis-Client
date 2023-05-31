package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.ViewProductImageResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse


interface RemoteViewProductImageDataSource {

    suspend fun viewProductImage(
        productId: String,
        token: String
    ): ViewProductImageResult<List<ViewProductResponse>>

}
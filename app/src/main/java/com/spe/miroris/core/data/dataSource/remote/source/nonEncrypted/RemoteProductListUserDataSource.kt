package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.ProductListUserResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse


interface RemoteProductListUserDataSource {

    suspend fun getProductList(
        page: String,
        limit: String,
        token: String
    ): ProductListUserResult<ProductListUserResponse>
}
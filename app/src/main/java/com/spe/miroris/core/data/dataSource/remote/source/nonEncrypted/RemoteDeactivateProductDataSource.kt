package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult


interface RemoteDeactivateProductDataSource {

    suspend fun deactivateProduct(
        id: String,
        productStatus: String,
        token: String
    ): DeactivateProductResult

}
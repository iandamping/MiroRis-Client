package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse


interface RemoteProductCatalogDataSource {

    suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName:String,
        categoryId: String,
        productType: String,
    ): ProductCatalogResult<ListProductCatalogResponse>

}
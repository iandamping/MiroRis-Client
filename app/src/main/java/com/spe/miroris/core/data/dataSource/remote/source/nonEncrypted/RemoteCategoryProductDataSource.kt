package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse


interface RemoteCategoryProductDataSource {


    suspend fun getCategoryProduct(
        page: String,
        limit: String,
    ): CategoryProductResult<ListCategoryProductResponse>
}
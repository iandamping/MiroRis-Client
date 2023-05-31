package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.EditProductCreateResult


interface RemoteEditProductDataSource {

    suspend fun editProduct(
        categoryId: String,
        email: String,
        productName: String,
        productDetail: String,
        productDuration: String,
        productTypePayment: String,
        productType: String,
        productStartFunding: String,
        productFinishFunding: String,
        productQris: String,
        productPaid: String,
        productStatus: String,
        token: String,
    ): EditProductCreateResult

}
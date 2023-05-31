package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.UploadProductResult
import okhttp3.MultipartBody


interface RemoteUploadProductDataSource {


    suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String,
        token: String
    ): UploadProductResult
}
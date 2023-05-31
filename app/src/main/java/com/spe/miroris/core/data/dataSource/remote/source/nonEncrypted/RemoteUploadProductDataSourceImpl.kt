package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadProductResult
import com.spe.miroris.core.data.dataSource.remote.model.request.UploadProductRequest
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteUploadProductDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteUploadProductDataSource, RemoteHelper by remoteHelper {
    override suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String,
        token: String
    ): UploadProductResult {
        return try {
            val encryptedProductId = encryptionManager.encryptRsa(productId)
            val signature = encryptionManager.createHmacSignature(encryptedProductId)
            when (val remoteData = nonEncryptionVoidCall(
                api.uploadProduct(
                    contentType = NetworkConstant.CONTENT_TYPE_MULTIPART,
                    tokenAuthorization = token,
                    request = UploadProductRequest(encryptedProductId, signature),
                    file = file
                )
            )) {
                is RemoteVoidResult.Error -> UploadProductResult.Error(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { RemoteHelperImpl.BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> UploadProductResult.Success
                        else -> UploadProductResult.Error(body.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            UploadProductResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }
}

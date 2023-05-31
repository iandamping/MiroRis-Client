package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult
import com.spe.miroris.core.data.dataSource.remote.model.request.DeactivateProductRequest
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteDeactivateProductDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteDeactivateProductDataSource, RemoteHelper by remoteHelper {
    override suspend fun deactivateProduct(
        id: String,
        productStatus: String,
        token: String
    ): DeactivateProductResult {
        val encryptedId = encryptionManager.encryptRsa(id)
        val encryptedProductStatus = encryptionManager.encryptRsa(productStatus)
        val signature =
            encryptionManager.createHmacSignature("$encryptedId:$encryptedProductStatus")

        return try {
            when (val remoteData = nonEncryptionVoidCall(
                api.deactivateProduct(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = DeactivateProductRequest(
                        productId = encryptedId,
                        productStatus = encryptedProductStatus,
                        signature = signature
                    )
                )
            )) {
                is RemoteVoidResult.Error -> DeactivateProductResult.Error(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> DeactivateProductResult.Success
                        else -> DeactivateProductResult.Error(body.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            DeactivateProductResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }

}
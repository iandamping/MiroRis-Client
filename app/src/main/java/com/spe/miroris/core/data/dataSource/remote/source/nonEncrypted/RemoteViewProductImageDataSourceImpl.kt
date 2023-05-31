package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.ViewProductImageResult
import com.spe.miroris.core.data.dataSource.remote.model.request.ViewProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteViewProductImageDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteViewProductImageDataSource, RemoteHelper by remoteHelper {

    override suspend fun viewProductImage(
        productId: String,
        token: String
    ): ViewProductImageResult<List<ViewProductResponse>> {
        return try {
            val encryptedProductId = encryptionManager.encryptRsa(productId)
            val signature = encryptionManager.createHmacSignature(encryptedProductId)
            return when (val remoteData =
                nonEncryptionCall(
                    api.viewProductImage(
                        contentType = NetworkConstant.CONTENT_TYPE,
                        tokenAuthorization = token,
                        request = ViewProductRequest(
                            productId = encryptedProductId,
                            signature = signature
                        )
                    )
                )) {
                is RemoteResult.Error -> ViewProductImageResult.Error(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { RemoteHelperImpl.BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> ViewProductImageResult.Success(
                            checkNotNull(data.data) { RemoteHelperImpl.DATA_NULL })

                        else -> ViewProductImageResult.Error(data.messageEnglish)
                    }
                }
            }

        } catch (e: Exception) {
            ViewProductImageResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
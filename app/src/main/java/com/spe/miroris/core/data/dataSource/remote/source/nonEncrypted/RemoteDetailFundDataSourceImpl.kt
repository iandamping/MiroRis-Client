package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.DetailFundResult
import com.spe.miroris.core.data.dataSource.remote.model.request.DetailFundRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteDetailFundDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteDetailFundDataSource, RemoteHelper by remoteHelper {
    override suspend fun detailFund(
        id: String,
        token: String
    ): DetailFundResult<DetailFundResponse> {
        return try {
            val encryptedId = encryptionManager.encryptRsa(id)
            val signature = encryptionManager.createHmacSignature(encryptedId)
            when (val remoteData = nonEncryptionCall(
                api.detailFund(
                    contentType = NetworkConstant.CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = DetailFundRequest(
                        id = encryptedId,
                        signature = signature
                    )
                )
            )) {
                is RemoteResult.Error -> DetailFundResult.Error(
                    remoteData.exception.message
                        ?: utilityHelper.getString(R.string.default_error_message)
                )

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { RemoteHelperImpl.BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            DetailFundResult.Success(checkNotNull(data.data) { RemoteHelperImpl.DATA_NULL })
                        }

                        else -> {
                            DetailFundResult.Error(data.messageEnglish)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            DetailFundResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }

    }

}
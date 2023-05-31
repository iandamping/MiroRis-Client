package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.GenerateQrResult
import com.spe.miroris.core.data.dataSource.remote.model.request.GenerateQrRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteQrisDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteQrisDataSource, RemoteHelper by remoteHelper {
    override suspend fun generateQr(
        productId: String,
        token: String
    ): GenerateQrResult<GenerateQrResponse> {
        return try {
            val encryptedProductId = encryptionManager.encryptRsa(productId)
            val signature = encryptionManager.createHmacSignature(encryptedProductId)
            when (val remoteData = nonEncryptionCall(
                api.generateQr(
                    contentType = NetworkConstant.CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = GenerateQrRequest(
                        productID = encryptedProductId,
                        signature = signature
                    )
                )
            )) {
                is RemoteResult.Error -> {
                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )
                    ) {
                        val errorMessage =
                            checkNotNull(remoteData.exception.message) { RemoteHelperImpl.ERROR_MESSAGE_NULL }
                        val types =
                            Types.newParameterizedType(
                                DefaultErrorBaseResponse::class.java,
                                DefaultErrorBaseResponse::class.java
                            )

                        val data = checkNotNull(
                            moshiParser.fromJson<DefaultErrorBaseResponse>(
                                errorMessage,
                                types
                            )
                        ) { RemoteHelperImpl.JSON_NULL }

                        when (data.status) {
                            ResponseStatus.RefreshToken.getCode() -> {
                                GenerateQrResult.RefreshToken
                            }

                            else -> GenerateQrResult.Error(
                                data.message
                            )
                        }
                    } else {
                        GenerateQrResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }

                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { RemoteHelperImpl.BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            GenerateQrResult.Success(
                                checkNotNull(
                                    data.data
                                ) { RemoteHelperImpl.DATA_NULL })
                        }

                        ResponseStatus.RefreshToken.getCode() -> GenerateQrResult.RefreshToken
                        else -> GenerateQrResult.Error(data.messageEnglish)
                    }
                }
            }

        } catch (e: Exception) {
            GenerateQrResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }


}
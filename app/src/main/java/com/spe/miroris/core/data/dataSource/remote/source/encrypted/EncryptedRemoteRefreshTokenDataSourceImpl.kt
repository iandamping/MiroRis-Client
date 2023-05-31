package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.JSON_NULL
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.RefreshTokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.RefreshTokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject


class EncryptedRemoteRefreshTokenDataSourceImpl @Inject constructor(
    @EncryptedApiInterfaceAnnotation private val api: EncryptedApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val moshiParser: MoshiParser,
    private val encryptionManager: EncryptionManager
) : EncryptedRemoteRefreshTokenDataSource, RemoteHelper by remoteHelper {

    override suspend fun refreshToken(
        email: String,
        token: String
    ): RefreshTokenRemoteResult<RefreshTokenResponse> {
        val encryptedEmail = encryptionManager.encryptRsa(email)
        val signature = encryptionManager.createHmacSignature(encryptedEmail)
        val request = RefreshTokenRequest(email = encryptedEmail, signature = signature)
        return try {
            when (val remoteData = encryptionCall(
                api.refreshToken(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = request
                )
            )) {
                RemoteEncryptedResult.EncryptionError -> RefreshTokenRemoteResult.EncryptionError

                is RemoteEncryptedResult.Error -> {
                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )
                    ) {
                        val decryptedErrorMessage =
                            checkNotNull(remoteData.exception.message) { RemoteHelperImpl.ERROR_MESSAGE_NULL }
                        val types =
                            Types.newParameterizedType(
                                DefaultErrorBaseResponse::class.java,
                                DefaultErrorBaseResponse::class.java
                            )

                        val data = checkNotNull(
                            moshiParser.fromJson<DefaultErrorBaseResponse>(
                                decryptedErrorMessage,
                                types
                            )
                        ) { JSON_NULL }

                        RefreshTokenRemoteResult.Error(
                            "code : ${data.code} error : ${data.message}"
                        )
                    } else {
                        RefreshTokenRemoteResult.Error(
                            remoteData.exception.message ?: utilityHelper.getString(
                                R.string.default_error_message
                            )
                        )
                    }
                }

                is RemoteEncryptedResult.Success -> {
                    val decryptedResult =
                        encryptionManager.decryptAes(checkNotNull(remoteData.data.body()) { BODY_NULL })

                    val types =
                        Types.newParameterizedType(
                            BaseResponse::class.java,
                            RefreshTokenResponse::class.java
                        )
                    val data =
                        checkNotNull(
                            moshiParser.fromJson<BaseResponse<RefreshTokenResponse>>(
                                decryptedResult, types
                            )
                        ) { JSON_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            val result = checkNotNull(data.data) { DATA_NULL }
                            val decryptToken = encryptionManager.decryptRsa(result.token)
                            RefreshTokenRemoteResult.Success(result.copy(token = decryptToken))
                        }

                        else -> RefreshTokenRemoteResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            RefreshTokenRemoteResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}




package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.JSON_NULL
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultDecryptedErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DecryptedErrorResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.security.keyProvider.KeyProvider
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class EncryptedRemoteTokenDataSourceImpl @Inject constructor(
    @EncryptedApiInterfaceAnnotation private val api: EncryptedApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val moshiParser: MoshiParser,
    private val encryptionManager: EncryptionManager,
    private val keyProvider: KeyProvider
) : EncryptedRemoteTokenDataSource, RemoteHelper by remoteHelper {

    companion object {
        private const val APP_VERSION = "1.9.0"
        private const val OS_VERSION = "android"
    }

    override suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
    ): TokenRemoteResult<TokenResponse> {
        val encryptedAuthVersion = encryptionManager.encryptRsa(
            data = keyProvider.provideAuthVersion()
        )
        val encryptedClientID = encryptionManager.encryptRsa(
            data = keyProvider.provideClientId()
        )
        val encryptedClientSecret = encryptionManager.encryptRsa(
            data = keyProvider.provideClientSecret()
        )
        val encryptedUUID = encryptionManager.encryptRsa(
            data = uuid
        )
        val signature = encryptionManager.createHmacSignature(
            value = "$encryptedAuthVersion:$encryptedClientID:$encryptedClientSecret:$encryptedUUID"
        )

        return try {
            when (val remoteData = encryptionCall(
                api.getToken(
                    contentType = CONTENT_TYPE, request = TokenRequest(
                        authVersion = encryptedAuthVersion,
                        clientId = encryptedClientID,
                        clientSecret = encryptedClientSecret,
                        uuid = encryptedUUID,
                        model = model,
                        brand = brand,
                        os = OS_VERSION,
                        appVersion = APP_VERSION,
                        signature = signature
                    )
                )
            )) {
                is RemoteEncryptedResult.Error -> {

                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )) {
                        val decryptedErrorMessage =
                            encryptionManager.decryptAes(checkNotNull(remoteData.exception.message) { RemoteHelperImpl.ERROR_MESSAGE_NULL })
                        val types =
                            Types.newParameterizedType(
                                DefaultDecryptedErrorBaseResponse::class.java,
                                DecryptedErrorResponse::class.java
                            )

                        val data = checkNotNull(
                            moshiParser.fromJson<DefaultDecryptedErrorBaseResponse<DecryptedErrorResponse>>(
                                decryptedErrorMessage,
                                types
                            )
                        ) { JSON_NULL }

                        TokenRemoteResult.Error(
                            "code : ${data.code} error : ${data.data.first().message}"
                        )
                    } else {
                        TokenRemoteResult.Error(
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
                            TokenResponse::class.java
                        )

                    val data =
                        checkNotNull(
                            moshiParser.fromJson<BaseResponse<TokenResponse>>(
                                decryptedResult, types
                            )
                        ) { JSON_NULL }

                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            val result = checkNotNull(data.data) { DATA_NULL }
                            val decryptToken = encryptionManager.decryptRsa(result.accessToken)
                            TokenRemoteResult.Success(result.copy(accessToken = decryptToken))
                        }

                        else -> TokenRemoteResult.Error(data.messageEnglish)
                    }
                }

                RemoteEncryptedResult.EncryptionError -> TokenRemoteResult.EncryptionError
            }
        } catch (e: Exception) {
            TokenRemoteResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }
}
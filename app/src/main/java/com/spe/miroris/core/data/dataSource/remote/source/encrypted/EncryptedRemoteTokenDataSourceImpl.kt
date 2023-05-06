package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.EncryptedRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class EncryptedRemoteTokenDataSourceImpl @Inject constructor(
    @EncryptedApiInterfaceAnnotation private val api: EncryptedApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val moshiParser: MoshiParser,
    private val encryptionManager: EncryptionManager
) : EncryptedRemoteTokenDataSource, RemoteHelper by remoteHelper {

    companion object {
        private const val JSON_NULL = "json is null"
        private const val BODY_NULL = "response body is null"
        private const val DATA_NULL = "data from service is null"
        private const val APP_VERSION = "1.9.0"
        private const val OS_VERSION = "android"
    }

    override suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String
    ): TokenRemoteResult<TokenResponse> {
        val encryptedAuthVersion = encryptionManager.encryptRsa(
            data = encryptionManager.provideAuthVersion()
        )
        val encryptedClientID = encryptionManager.encryptRsa(
            data = encryptionManager.provideClientId()
        )
        val encryptedClientSecret = encryptionManager.encryptRsa(
            data = encryptionManager.provideClientSecret()
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
                    TokenRequest(
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
                is EncryptedRemoteResult.Error -> TokenRemoteResult.SourceError(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is EncryptedRemoteResult.Success -> {
                    val types =
                        Types.newParameterizedType(
                            BaseResponse::class.java,
                            TokenResponse::class.java
                        )

                    val data =
                        checkNotNull(
                            moshiParser.fromJson<BaseResponse<TokenResponse>>(
                                checkNotNull(remoteData.data.body()) { BODY_NULL }, types
                            )
                        ) { JSON_NULL }

                    when (data.code) {
                        ResponseStatus.Success.getCode() -> TokenRemoteResult.SourceData(
                            checkNotNull(data.data) { DATA_NULL }
                        )

                        else -> TokenRemoteResult.SourceError(data.message)
                    }
                }

                EncryptedRemoteResult.EncryptionError -> TokenRemoteResult.EncryptionError
            }
        } catch (e: Exception) {
            TokenRemoteResult.SourceError(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }
}
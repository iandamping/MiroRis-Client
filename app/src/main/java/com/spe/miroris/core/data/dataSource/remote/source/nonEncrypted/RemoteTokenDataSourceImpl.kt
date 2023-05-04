package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteTokenDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteTokenDataSource, RemoteHelper by remoteHelper {

    companion object {
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
            publicKey = encryptionManager.provideRsaPublicKey(),
            data = encryptionManager.provideAuthVersion()
        )
        val encryptedClientID = encryptionManager.encryptRsa(
            publicKey = encryptionManager.provideRsaPublicKey(),
            data = encryptionManager.provideClientId()
        )
        val encryptedClientSecret = encryptionManager.encryptRsa(
            publicKey = encryptionManager.provideRsaPublicKey(),
            data = encryptionManager.provideClientSecret()
        )
        val encryptedUUID = encryptionManager.encryptRsa(
            publicKey = encryptionManager.provideRsaPublicKey(),
            data = uuid
        )
        val signature = encryptionManager.createHmacSignature(
            hmacKey = encryptionManager.provideHmac512Key(),
            value = "$encryptedAuthVersion:$encryptedClientID:$encryptedClientSecret:$encryptedUUID"
        )
        return try {
            when (val remoteData = nonEncryptionCall(
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
                is RemoteResult.Error -> TokenRemoteResult.SourceError(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteResult.Success -> {
                    val body = remoteData.data.body()
                    when (checkNotNull(body) { BODY_NULL }.code) {
                        ResponseStatus.Success.getCode() -> TokenRemoteResult.SourceData(
                            checkNotNull(body.data) { DATA_NULL }
                        )

                        else -> TokenRemoteResult.SourceError(body.message)
                    }
                }
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
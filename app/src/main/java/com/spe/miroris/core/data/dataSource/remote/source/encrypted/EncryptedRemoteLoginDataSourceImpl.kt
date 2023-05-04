package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.EncryptedRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class EncryptedRemoteLoginDataSourceImpl @Inject constructor(
    @EncryptedApiInterfaceAnnotation private val api: EncryptedApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val moshiParser: MoshiParser,
    private val encryptionManager: EncryptionManager
) : EncryptedRemoteLoginDataSource, RemoteHelper by remoteHelper {

    companion object {
        private const val JSON_NULL = "json is null"
        private const val BODY_NULL = "response body is null"
        private const val DATA_NULL = "data from service is null"
    }

    override suspend fun userLogin(
        email: String,
        password: String
    ): LoginRemoteResult<LoginResponse> {

        return try {
            val encryptedEmail = encryptionManager.encryptRsa(
                data = email,
                publicKey = encryptionManager.provideRsaPublicKey(),
            )
            val encryptedPassword = encryptionManager.encryptRsa(
                data = password,
                publicKey = encryptionManager.provideRsaPublicKey(),
            )
            val signature = encryptionManager.createHmacSignature(
                hmacKey = encryptionManager.provideHmac512Key(),
                value = "$encryptedEmail:$encryptedPassword"
            )

            val request = LoginRequest(
                email = encryptedEmail,
                password = encryptedPassword,
                signature = signature
            )
            when (val remoteData = encryptionCall(api.userLogin(request))) {
                EncryptedRemoteResult.EncryptionError -> LoginRemoteResult.EncryptionError
                is EncryptedRemoteResult.Error -> LoginRemoteResult.SourceError(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is EncryptedRemoteResult.Success -> {
                    val types =
                        Types.newParameterizedType(
                            BaseResponse::class.java,
                            LoginResponse::class.java
                        )

                    val data =
                        checkNotNull(
                            moshiParser.fromJson<BaseResponse<LoginResponse>>(
                                checkNotNull(remoteData.data.body()) { BODY_NULL }, types
                            )
                        ) { JSON_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            return LoginRemoteResult.SourceData(checkNotNull(data.data) { DATA_NULL })
                        }

                        else -> return LoginRemoteResult.SourceError(data.message)
                    }
                }
            }
        } catch (e: Exception) {
            LoginRemoteResult.SourceError(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }
}
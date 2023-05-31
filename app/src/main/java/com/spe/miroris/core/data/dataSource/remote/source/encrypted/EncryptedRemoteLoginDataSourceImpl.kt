package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.ERROR_MESSAGE_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.JSON_NULL
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultDecryptedErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DecryptedErrorResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import timber.log.Timber
import javax.inject.Inject

class EncryptedRemoteLoginDataSourceImpl @Inject constructor(
    @EncryptedApiInterfaceAnnotation private val api: EncryptedApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val moshiParser: MoshiParser,
    private val encryptionManager: EncryptionManager
) : EncryptedRemoteLoginDataSource, RemoteHelper by remoteHelper {

    override suspend fun userLogin(
        email: String,
        password: String,
        fcmId: String,
        token: String
    ): LoginRemoteResult<LoginResponse> {

        return try {
            val encryptedEmail = encryptionManager.encryptRsa(
                data = email,
            )
            val encryptedPassword = encryptionManager.encryptRsa(
                data = password,
            )
            val encryptedFcmId = encryptionManager.encryptRsa(data = fcmId)

            val signature = encryptionManager.createHmacSignature(
                value = "$encryptedEmail:$encryptedPassword"
            )

            val request = LoginRequest(
                email = encryptedEmail,
                password = encryptedPassword,
                fcmId = encryptedFcmId,
                signature = signature
            )

            when (val remoteData = encryptionCall(
                api.userLogin(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = request
                )
            )) {
                RemoteEncryptedResult.EncryptionError -> LoginRemoteResult.EncryptionError
                is RemoteEncryptedResult.Error -> {
                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )
                    ) {
                        val decryptedErrorMessage =
                            encryptionManager.decryptAes(checkNotNull(remoteData.exception.message) { ERROR_MESSAGE_NULL })
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

                        LoginRemoteResult.Error(
                            "code : ${data.code} error : ${data.data.first().message}"
                        )
                    } else {
                        LoginRemoteResult.Error(
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
                            LoginResponse::class.java
                        )

                    val data =
                        checkNotNull(
                            moshiParser.fromJson<BaseResponse<LoginResponse>>(
                                decryptedResult,
                                types
                            )
                        ) { JSON_NULL }
                    Timber.e("${data.code}")
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            val result = checkNotNull(data.data) { DATA_NULL }
                            val decryptToken = encryptionManager.decryptRsa(result.userToken)
                            return LoginRemoteResult.Success(result.copy(userToken = decryptToken))
                        }

                        ResponseStatus.LoginOnUse.getCode() -> {
                            return LoginRemoteResult.Error(data.messageEnglish)
                        }

                        else -> return LoginRemoteResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            LoginRemoteResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }
}
package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.LogoutRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.LogoutRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteLogoutDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteLogoutDataSource, RemoteHelper by remoteHelper {

    override suspend fun logoutUser(
        email: String,
        uuid: String,
        token: String
    ): LogoutRemoteResult {
        val encryptedUUID = encryptionManager.encryptRsa(uuid)
        val encryptedEmail = encryptionManager.encryptRsa(email)
        val signature = encryptionManager.createHmacSignature("$encryptedUUID:$encryptedEmail")

        return try {
            when (val remoteData = nonEncryptionVoidCall(
                api.logoutUser(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = LogoutRequest(
                        uuid = encryptedUUID,
                        email = encryptedEmail,
                        signature = signature
                    )
                )
            )) {
                is RemoteVoidResult.Error -> {
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
                                LogoutRemoteResult.RefreshToken
                            }

                            else -> LogoutRemoteResult.Error(
                                data.message
                            )
                        }


                    } else {
                        LogoutRemoteResult.Error(
                            remoteData.exception.message ?: utilityHelper.getString(
                                R.string.default_error_message
                            )
                        )
                    }
                }

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> LogoutRemoteResult.Success
                        else -> LogoutRemoteResult.Error(body.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            LogoutRemoteResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }

    }

}
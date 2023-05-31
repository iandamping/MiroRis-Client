package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.ResetPasswordResult
import com.spe.miroris.core.data.dataSource.remote.model.request.ResetPasswordRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteResetPasswordDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteResetPasswordDataSource, RemoteHelper by remoteHelper {

    override suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        token: String
    ): ResetPasswordResult {
        return try {
            val encryptedCurrentPassword = encryptionManager.encryptRsa(currentPassword)
            val encryptedNewPassword = encryptionManager.encryptRsa(newPassword)
            val encryptedConfirmPassword = encryptionManager.encryptRsa(confirmPassword)
            val signature =
                encryptionManager.createHmacSignature("$encryptedCurrentPassword:$encryptedNewPassword:$encryptedConfirmPassword")

            when (val remoteData = nonEncryptionVoidCall(
                api.resetPassword(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = ResetPasswordRequest(
                        encryptedCurrentPassword,
                        encryptedNewPassword,
                        encryptedConfirmPassword,
                        signature
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
                                ResetPasswordResult.RefreshToken
                            }

                            else -> ResetPasswordResult.Error(
                                data.message
                            )
                        }
                    } else {
                        ResetPasswordResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }
                }

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> ResetPasswordResult.Success
                        ResponseStatus.RefreshToken.getCode() -> ResetPasswordResult.RefreshToken
                        else -> ResetPasswordResult.Error(body.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            ResetPasswordResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }

}
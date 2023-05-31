package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.RegisterRequest
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteRegisterDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteRegisterDataSource, RemoteHelper by remoteHelper {

    override suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult {
        return try {
            val encryptedEmail = encryptionManager.encryptRsa(
                data = email
            )
            val encryptedPassword = encryptionManager.encryptRsa(
                data = password
            )
            val encryptedConfirmPassword = encryptionManager.encryptRsa(
                data = confirmPassword
            )

            val signature = encryptionManager.createHmacSignature(
                value = "$encryptedEmail:$encryptedPassword:$encryptedConfirmPassword"
            )

            when (val remoteData = nonEncryptionCall(
                api.registerUser(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = RegisterRequest(
                        email = encryptedEmail,
                        password = encryptedPassword,
                        confirmPassword = encryptedConfirmPassword,
                        signature = signature
                    )
                )
            )) {
                is RemoteResult.Error -> RegisterRemoteResult.Error(
                    remoteData.exception.message
                        ?: utilityHelper.getString(R.string.default_error_message)
                )

                is RemoteResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }


                    when (body.code) {
                        ResponseStatus.Success.getCode() -> RegisterRemoteResult.Success
                        else -> {
                            if (body.data == null) {
                                RegisterRemoteResult.Error("code : ${body.code} error : ${body.messageEnglish}")
                            } else RegisterRemoteResult.Error("code : ${body.code} error : ${body.data.errorEmail.first()} & ${body.data.errorPassword.first()}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            RegisterRemoteResult.Error(
                e.message
                    ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
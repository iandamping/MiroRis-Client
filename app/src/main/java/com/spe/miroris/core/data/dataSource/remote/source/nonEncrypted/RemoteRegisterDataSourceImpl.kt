package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
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

        return when (val remoteData = nonEncryptionVoidCall(
            api.registerUser(
                contentType = CONTENT_TYPE,
                tokenAuthorization = "Bearer $token",
                request = RegisterRequest(
                    email = encryptedEmail,
                    password = encryptedPassword,
                    confirmPassword = encryptedConfirmPassword,
                    signature = signature
                )
            )
        )) {
            is RemoteVoidResult.Error -> RegisterRemoteResult.SourceError(
                remoteData.exception.message
                    ?: utilityHelper.getString(R.string.default_error_message)
            )

            RemoteVoidResult.Success -> RegisterRemoteResult.Success
        }
    }

}
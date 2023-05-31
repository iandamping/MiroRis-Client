package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.EditUserResult
import com.spe.miroris.core.data.dataSource.remote.model.request.EditUserRequest
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import javax.inject.Inject

class RemoteEditUserDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteEditUserDataSource, RemoteHelper by remoteHelper {

    override suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String,
        token: String
    ): EditUserResult {
        return try {
            val encryptedUserName = encryptionManager.encryptRsa(username)
            val encryptedEmail = encryptionManager.encryptRsa(email)
            val encryptedAddress = encryptionManager.encryptRsa(address)
            val encryptedPhoneNumber = encryptionManager.encryptRsa(phoneNumber)
            val encryptedCity = encryptionManager.encryptRsa(city)
            val encryptedBankCode = encryptionManager.encryptRsa(bankCode)
            val encryptedAccNumber = encryptionManager.encryptRsa(accountNumber)
            val signature =
                encryptionManager.createHmacSignature("$encryptedUserName:$encryptedEmail:$encryptedAddress:$encryptedPhoneNumber")

            when (val remoteData = nonEncryptionVoidCall(
                api.editUser(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = EditUserRequest(
                        email = encryptedEmail,
                        username = encryptedUserName,
                        address = encryptedAddress,
                        phoneNumber = encryptedPhoneNumber,
                        city = encryptedCity,
                        bankCode = encryptedBankCode,
                        accountNumber = encryptedAccNumber,
                        signature = signature
                    )
                )
            )) {
                is RemoteVoidResult.Error -> EditUserResult.Error(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> EditUserResult.Success
                        else -> EditUserResult.Error(body.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            EditUserResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }

}
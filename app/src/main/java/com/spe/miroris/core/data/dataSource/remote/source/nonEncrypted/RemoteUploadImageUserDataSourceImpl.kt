package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadResult
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import reduceFileImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class RemoteUploadImageUserDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager
) : RemoteUploadImageUserDataSource, RemoteHelper by remoteHelper {

    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    override suspend fun uploadUserImage(
        file: File?,
        email: String,
        token: String
    ): UploadResult {
        return try {
            val encryptedEmail = encryptionManager.encryptRsa(email)
            val signature = encryptionManager.createHmacSignature(encryptedEmail)

            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis())

            builder
                .addFormDataPart(
                    "file", "$name.png",
                    reduceFileImage(checkNotNull(file)).asRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("email", encryptedEmail)
                .addFormDataPart("signature", signature)

            when (val remoteData = nonEncryptionVoidCall(
                api.uploadImageUser(
                    tokenAuthorization = token,
                    data = builder.build()
                )
            )) {
                is RemoteVoidResult.Error -> UploadResult.Error(
                    remoteData.exception.message ?: utilityHelper.getString(
                        R.string.default_error_message
                    )
                )

                is RemoteVoidResult.Success -> {
                    val body = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (body.code) {
                        ResponseStatus.Success.getCode() -> UploadResult.Success
                        else -> UploadResult.Error(body.messageEnglish)
                    }
                }
            }

        } catch (e: Exception) {
            UploadResult.Error(
                e.message ?: utilityHelper.getString(
                    R.string.default_error_message
                )
            )
        }
    }

}
package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.ProfileRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.request.ProfileRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProfileResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteProfileDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteProfileDataSource, RemoteHelper by remoteHelper {

    override suspend fun getProfile(
        email: String,
        token: String
    ): ProfileRemoteResult<ProfileResponse> {
        return try {
            val encryptedEmail = encryptionManager.encryptRsa(email)
            val signature = encryptionManager.createHmacSignature(encryptedEmail)
            when (val remoteData = nonEncryptionCall(
                api.getProfile(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = ProfileRequest(email = encryptedEmail, signature = signature)
                )
            )) {
                is RemoteResult.Error -> {
                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )) {
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

                        when(data.status){
                            ResponseStatus.RefreshToken.getCode() ->{
                                ProfileRemoteResult.RefreshToken
                            }
                            else ->  ProfileRemoteResult.Error(
                                data.message
                            )
                        }
                    } else {
                        ProfileRemoteResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }

                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()){ BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> ProfileRemoteResult.Success(
                            checkNotNull(
                                data.data
                            ) { DATA_NULL })

                        ResponseStatus.RefreshToken.getCode() -> ProfileRemoteResult.RefreshToken
                        else -> ProfileRemoteResult.Error(data.messageEnglish)
                    }
                }
            }

        } catch (e: Exception) {
            ProfileRemoteResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
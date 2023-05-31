package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.request.InvestmentsRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteInvestmentsDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteInvestmentsDataSource, RemoteHelper by remoteHelper {

    override suspend fun getInvestments(
        page: String,
        limit: String,
        token: String
    ): InvestmentsResult<ListInvestmentResponse> {
        return try {
            val encryptedPage = encryptionManager.encryptRsa(page)
            val encryptedLimit = encryptionManager.encryptRsa(limit)
            val signature = encryptionManager.createHmacSignature("$encryptedPage:$encryptedLimit")
            when (val remoteData = nonEncryptionCall(
                api.getInvestments(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = InvestmentsRequest(
                        page = encryptedPage,
                        limit = encryptedLimit,
                        signature = signature
                    )
                )
            )) {
                is RemoteResult.Error -> {
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
                                InvestmentsResult.RefreshToken
                            }

                            else -> InvestmentsResult.Error(
                                data.message
                            )
                        }
                    } else {
                        InvestmentsResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }
                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { RemoteHelperImpl.BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {
                            InvestmentsResult.Success(checkNotNull(data.data) { RemoteHelperImpl.DATA_NULL })
                        }

                        ResponseStatus.RefreshToken.getCode() -> {
                            InvestmentsResult.RefreshToken
                        }

                        else -> {
                            InvestmentsResult.Error(data.messageEnglish)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            InvestmentsResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.request.BankNameRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.BankRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteBanksDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteBanksDataSource, RemoteHelper by remoteHelper {

    override suspend fun getBank(
        page: String,
        limit: String,
        bankName: String,
        token: String
    ): BanksResult<ListBankResponse> {
        return try {
            val encryptedLimit = encryptionManager.encryptRsa(limit)
            val signature = encryptionManager.createHmacSignature(encryptedLimit)

            when (val remoteData = nonEncryptionCall(
                api.getBanks(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = BankRequest(
                        limit = encryptedLimit,
                        search = BankNameRequest(bankName = bankName),
                        signature = signature
                    )
                )
            )) {
                is RemoteResult.Error -> {
                    if (remoteData.exception.message != utilityHelper.getString(
                            R.string.default_error_message
                        )
                    ) {
                        val decryptedErrorMessage =
                            checkNotNull(remoteData.exception.message) { RemoteHelperImpl.ERROR_MESSAGE_NULL }
                        val types =
                            Types.newParameterizedType(
                                DefaultErrorBaseResponse::class.java,
                                DefaultErrorBaseResponse::class.java
                            )

                        val data = checkNotNull(
                            moshiParser.fromJson<DefaultErrorBaseResponse>(
                                decryptedErrorMessage,
                                types
                            )
                        ) { RemoteHelperImpl.JSON_NULL }

                        when (data.status) {
                            ResponseStatus.RefreshToken.getCode() -> {
                                BanksResult.RefreshToken
                            }

                            else -> BanksResult.Error(
                                "code : ${data.code} error : ${data.message}"
                            )
                        }


                    } else {
                        BanksResult.Error(
                            remoteData.exception.message ?: utilityHelper.getString(
                                R.string.default_error_message
                            )
                        )
                    }
                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> {

                            BanksResult.Success(checkNotNull(data.data) { DATA_NULL })
                        }

                        ResponseStatus.RefreshToken.getCode() -> {
                            BanksResult.RefreshToken
                        }

                        else -> {
                            BanksResult.Error(data.messageEnglish)
                        }

                    }
                }
            }
        } catch (e: Exception) {
            BanksResult.Error(e.message ?: utilityHelper.getString(R.string.default_error_message))
        }
    }

}
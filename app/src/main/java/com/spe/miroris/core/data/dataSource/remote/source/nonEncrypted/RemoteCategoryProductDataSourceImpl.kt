package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.request.CategoryRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteCategoryProductDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteCategoryProductDataSource, RemoteHelper by remoteHelper {

    override suspend fun getCategoryProduct(
        page: String,
        limit: String,
    ): CategoryProductResult<ListCategoryProductResponse> {
        val encryptedPage = encryptionManager.encryptRsa(page)
        val encryptedLimit = encryptionManager.encryptRsa(limit)
        val signature = encryptionManager.createHmacSignature("$encryptedPage:$encryptedLimit")

        return try {
            when (val remoteData = nonEncryptionCall(
                api.categoryProduct(
                    contentType = NetworkConstant.CONTENT_TYPE,
                    request = CategoryRequest(
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

                        CategoryProductResult.Error(
                            data.message
                        )
                    } else {
                        CategoryProductResult.Error(
                            remoteData.exception.message ?: utilityHelper.getString(
                                R.string.default_error_message
                            )
                        )
                    }
                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> CategoryProductResult.Success(
                            checkNotNull(data.data) { DATA_NULL })

                        else -> CategoryProductResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            CategoryProductResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
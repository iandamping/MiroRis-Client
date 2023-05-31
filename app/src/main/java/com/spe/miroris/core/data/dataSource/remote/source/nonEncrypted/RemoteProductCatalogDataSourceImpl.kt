package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.DATA_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.request.CatalogRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ProductCatalogRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteProductCatalogDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteProductCatalogDataSource, RemoteHelper by remoteHelper {


    override suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName:String,
        categoryId: String,
        productType: String,
    ): ProductCatalogResult<ListProductCatalogResponse> {
        val encryptedPage = encryptionManager.encryptRsa(page)
        val encryptedLimit = encryptionManager.encryptRsa(limit)
        val signature = encryptionManager.createHmacSignature("$encryptedPage:$encryptedLimit")

        return try {
            when (val remoteData = nonEncryptionCall(
                api.productCatalog(
                    contentType = CONTENT_TYPE,
                    request = ProductCatalogRequest(
                        page = encryptedPage,
                        limit = encryptedLimit,
                        search = CatalogRequest(
                            productName = productName,
                            categoryId = categoryId,
                            productType = productType
                        ),
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

                        ProductCatalogResult.Error(
                            data.message
                        )
                    } else {
                        ProductCatalogResult.Error(
                            remoteData.exception.message ?: utilityHelper.getString(
                                R.string.default_error_message
                            )
                        )
                    }
                }

                is RemoteResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> ProductCatalogResult.Success(
                            checkNotNull(data.data) { DATA_NULL })

                        else -> ProductCatalogResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            ProductCatalogResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
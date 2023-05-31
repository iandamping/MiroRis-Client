package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.EditProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.request.EditProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteEditProductDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteEditProductDataSource, RemoteHelper by remoteHelper {

    override suspend fun editProduct(
        categoryId: String,
        email: String,
        productName: String,
        productDetail: String,
        productDuration: String,
        productTypePayment: String,
        productType: String,
        productStartFunding: String,
        productFinishFunding: String,
        productQris: String,
        productPaid: String,
        productStatus: String,
        token: String
    ): EditProductCreateResult {
        return try {
            val encryptedCategoryId = encryptionManager.encryptRsa(categoryId)
            val encryptedEmail = encryptionManager.encryptRsa(email)
            val encryptedProductName = encryptionManager.encryptRsa(productName)
            val encryptedProductDetail = encryptionManager.encryptRsa(productDetail)
            val encryptedProductDuration = encryptionManager.encryptRsa(productDuration)
            val encryptedProductTypePayment = encryptionManager.encryptRsa(productTypePayment)
            val encryptedProductType = encryptionManager.encryptRsa(productType)
            val encryptedProductStartFunding = encryptionManager.encryptRsa(productStartFunding)
            val encryptedProductFinishFunding = encryptionManager.encryptRsa(productFinishFunding)
            val encryptedProductQris = encryptionManager.encryptRsa(productQris)
            val encryptedProductPaid = encryptionManager.encryptRsa(productPaid)
            val encryptedProductStatus = encryptionManager.encryptRsa(productStatus)
            val signature =
                encryptionManager.createHmacSignature("$encryptedCategoryId:$encryptedEmail:$encryptedProductName:$encryptedProductDetail")

            when (val remoteData = nonEncryptionVoidCall(
                api.editProductCreate(
                    contentType = NetworkConstant.CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = EditProductRequest(
                        categoryId = encryptedCategoryId,
                        email = encryptedEmail,
                        productName = encryptedProductName,
                        productDetail = encryptedProductDetail,
                        productDuration = encryptedProductDuration,
                        productTypePayment = encryptedProductTypePayment,
                        productType = encryptedProductType,
                        productStartFunding = encryptedProductStartFunding,
                        productFinishFunding = encryptedProductFinishFunding,
                        productQris = encryptedProductQris,
                        productPaid = encryptedProductPaid,
                        productStatus = encryptedProductStatus,
                        signature = signature
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
                                EditProductCreateResult.RefreshToken
                            }

                            else -> EditProductCreateResult.Error(
                                data.message
                            )
                        }
                    } else {
                        EditProductCreateResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }

                }

                is RemoteVoidResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> EditProductCreateResult.Success
                        ResponseStatus.RefreshToken.getCode() -> EditProductCreateResult.RefreshToken
                        else -> EditProductCreateResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            EditProductCreateResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }

    }

}
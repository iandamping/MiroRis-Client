package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl.Companion.BODY_NULL
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.ResponseStatus
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.request.ProductCreateRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CONTENT_TYPE
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.squareup.moshi.Types
import javax.inject.Inject

class RemoteProductCreateDataSourceImpl @Inject constructor(
    @DefaultApiInterfaceAnnotation private val api: ApiInterface,
    private val remoteHelper: RemoteHelper,
    private val utilityHelper: UtilityHelper,
    private val encryptionManager: EncryptionManager,
    private val moshiParser: MoshiParser
) : RemoteProductCreateDataSource, RemoteHelper by remoteHelper {
    override suspend fun productCreate(
        categoryId: String,
        email: String,
        productName: String,
        productDetail: String,
        productDuration: String,
        productTypePayment: String,
        productType: String,
        productStartFunding: String,
        productFinishFunding: String,
        productBankCode: String,
        productAccountNumber: String,
        personalAccount: String,
        productStatus: String,
        token: String
    ): ProductCreateResult {
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
            val encryptedProductBank = encryptionManager.encryptRsa(productBankCode)
            val encryptedProductAccountNumber = encryptionManager.encryptRsa(productAccountNumber)
            val encryptedPersonalAccount = encryptionManager.encryptRsa(personalAccount)
            val encryptedProductStatus = encryptionManager.encryptRsa(productStatus)
            val signature =
                encryptionManager.createHmacSignature("$encryptedCategoryId:$encryptedEmail:$encryptedProductName")

            when (val remoteData = nonEncryptionVoidCall(
                api.productCreate(
                    contentType = CONTENT_TYPE,
                    tokenAuthorization = token,
                    request = ProductCreateRequest(
                        categoryId = encryptedCategoryId,
                        email = encryptedEmail,
                        productName = encryptedProductName,
                        productDetail = encryptedProductDetail,
                        productDuration = encryptedProductDuration,
                        productTypePayment = encryptedProductTypePayment,
                        productType = encryptedProductType,
                        productStartFunding = encryptedProductStartFunding,
                        productFinishFunding = encryptedProductFinishFunding,
                        productBankCode = encryptedProductBank,
                        productAccountNumber = encryptedProductAccountNumber,
                        productPersonalAccount = encryptedPersonalAccount,
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
                                ProductCreateResult.RefreshToken
                            }

                            else -> ProductCreateResult.Error(
                                data.message
                            )
                        }
                    } else {
                        ProductCreateResult.Error(
                            remoteData.exception.message
                                ?: utilityHelper.getString(R.string.default_error_message)
                        )
                    }
                }

                is RemoteVoidResult.Success -> {
                    val data = checkNotNull(remoteData.data.body()) { BODY_NULL }
                    when (data.code) {
                        ResponseStatus.Success.getCode() -> ProductCreateResult.Success
                        ResponseStatus.RefreshToken.getCode() -> ProductCreateResult.RefreshToken
                        else -> ProductCreateResult.Error(data.messageEnglish)
                    }
                }
            }
        } catch (e: Exception) {
            ProductCreateResult.Error(
                e.message ?: utilityHelper.getString(R.string.default_error_message)
            )
        }
    }

}
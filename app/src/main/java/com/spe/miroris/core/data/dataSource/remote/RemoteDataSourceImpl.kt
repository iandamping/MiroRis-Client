package com.spe.miroris.core.data.dataSource.remote

import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DetailFundResult
import com.spe.miroris.core.data.dataSource.remote.model.common.EditProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.EditUserResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FirebaseIdResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FundsResult
import com.spe.miroris.core.data.dataSource.remote.model.common.GenerateQrResult
import com.spe.miroris.core.data.dataSource.remote.model.common.InvestmentsResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LogoutRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductListUserResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProfileRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ResetPasswordResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ViewProductImageResult
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProfileResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.RefreshTokenResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteLoginDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteRefreshTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteBanksDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteCategoryProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteDeactivateProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteDetailFundDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFirebaseIdDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFundsDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteInvestmentsDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteLogoutDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCatalogDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCreateDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductListUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProfileDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteQrisDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteRegisterDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteResetPasswordDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadImageUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteViewProductImageDataSource
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val encryptedRemoteTokenDataSource: EncryptedRemoteTokenDataSource,
    private val encryptedRemoteLoginDataSource: EncryptedRemoteLoginDataSource,
    private val encryptedRemoteRefreshTokenDataSource: EncryptedRemoteRefreshTokenDataSource,
    private val firebaseIdDataSource: RemoteFirebaseIdDataSource,
    private val logoutDataSource: RemoteLogoutDataSource,
    private val registerDataSource: RemoteRegisterDataSource,
    private val editUserDataSource: RemoteEditUserDataSource,
    private val profileDataSource: RemoteProfileDataSource,
    private val banksDataSource: RemoteBanksDataSource,
    private val categoryProductDataSource: RemoteCategoryProductDataSource,
    private val productListUserDataSource: RemoteProductListUserDataSource,
    private val uploadImageUserDataSource: RemoteUploadImageUserDataSource,
    private val productCatalogDataSource: RemoteProductCatalogDataSource,
    private val productCreateDataSource: RemoteProductCreateDataSource,
    private val editProductCreateDataSource: RemoteEditProductDataSource,
    private val deactivateProductDataSource: RemoteDeactivateProductDataSource,
    private val fundsDataSource: RemoteFundsDataSource,
    private val investmentsDataSource: RemoteInvestmentsDataSource,
    private val detailFundDataSource: RemoteDetailFundDataSource,
    private val uploadProductDataSource: RemoteUploadProductDataSource,
    private val viewProductImageDataSource: RemoteViewProductImageDataSource,
    private val resetPasswordDataSource: RemoteResetPasswordDataSource,
    private val remoteQrisDataSource: RemoteQrisDataSource
) : RemoteDataSource {
    override suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
    ): TokenRemoteResult<TokenResponse> {
        return encryptedRemoteTokenDataSource.getToken(
            uuid = uuid,
            model = model,
            brand = brand,
            os = os,
        )
    }

    override suspend fun getFirebaseToken(): FirebaseIdResult<String> {
        return firebaseIdDataSource.getFirebaseToken()
    }


    override suspend fun userLogin(
        email: String,
        password: String,
        token: String,
        fcmId: String
    ): LoginRemoteResult<LoginResponse> {
        return encryptedRemoteLoginDataSource.userLogin(
            email = email,
            password = password,
            fcmId = fcmId,
            token = token
        )
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult {
        return registerDataSource.registerUser(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            token = token
        )
    }

    override suspend fun getProfile(
        email: String,
        token: String
    ): ProfileRemoteResult<ProfileResponse> {
        return profileDataSource.getProfile(email = email, token = token)
    }

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
        return editUserDataSource.editUser(
            username = username,
            email = email,
            address = address,
            phoneNumber = phoneNumber,
            city = city,
            bankCode = bankCode,
            accountNumber = accountNumber,
            token = token
        )
    }

    override suspend fun refreshToken(
        email: String,
        token: String
    ): RefreshTokenRemoteResult<RefreshTokenResponse> {
        return encryptedRemoteRefreshTokenDataSource.refreshToken(email, token)
    }

    override suspend fun getBank(
        page: String,
        limit: String,
        bankName: String,
        token: String
    ): BanksResult<ListBankResponse> {
        return banksDataSource.getBank(
            page = page,
            limit = limit,
            bankName = bankName,
            token = token
        )
    }

    override suspend fun getProductListUser(
        page: String,
        limit: String,
        token: String
    ): ProductListUserResult<ProductListUserResponse> {
        return productListUserDataSource.getProductList(page = page, limit = limit, token = token)
    }

    override suspend fun uploadUserImage(
        file: File?,
        email: String,
        token: String
    ): UploadResult {
        return uploadImageUserDataSource.uploadUserImage(file = file, email = email, token = token)
    }

    override suspend fun getCategoryProduct(
        page: String,
        limit: String
    ): CategoryProductResult<ListCategoryProductResponse> {
        return categoryProductDataSource.getCategoryProduct(page = page, limit = limit)
    }

    override suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName: String,
        categoryId: String,
        productType: String,
    ): ProductCatalogResult<ListProductCatalogResponse> {
        return productCatalogDataSource.getProductCatalog(
            page = page,
            limit = limit,
            productName = productName,
            categoryId = categoryId,
            productType = productType,
        )
    }

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
        token:String
    ): ProductCreateResult {
        return productCreateDataSource.productCreate(
            categoryId = categoryId,
            email = email,
            productName = productName,
            productDetail = productDetail,
            productDuration = productDuration,
            productTypePayment = productTypePayment,
            productType = productType,
            productStartFunding = productStartFunding,
            productFinishFunding = productFinishFunding,
            productBankCode = productBankCode,
            productAccountNumber = productAccountNumber,
            personalAccount = personalAccount,
            productStatus = productStatus,
            token = token
        )
    }

    override suspend fun editProductCreate(
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
        return editProductCreateDataSource.editProduct(
            categoryId = categoryId,
            email = email,
            productName = productName,
            productDetail = productDetail,
            productDuration = productDuration,
            productTypePayment = productTypePayment,
            productType = productType,
            productStartFunding = productStartFunding,
            productFinishFunding = productFinishFunding,
            productQris = productQris,
            productPaid = productPaid,
            productStatus = productStatus,
            token = token
        )
    }

    override suspend fun deactivateProduct(
        id: String,
        productStatus: String,
        token: String
    ): DeactivateProductResult {
        return deactivateProductDataSource.deactivateProduct(
            id = id,
            productStatus = productStatus,
            token = token
        )
    }

    override suspend fun getFunds(
        page: String,
        limit: String,
        token: String
    ): FundsResult<ListFundResponse> {
        return fundsDataSource.getFunds(page = page, limit = limit, token = token)
    }

    override suspend fun getInvestments(
        page: String,
        limit: String,
        token: String
    ): InvestmentsResult<ListInvestmentResponse> {
        return investmentsDataSource.getInvestments(page = page, limit = limit, token = token)
    }

    override suspend fun detailFund(
        id: String,
        token: String
    ): DetailFundResult<DetailFundResponse> {
        return detailFundDataSource.detailFund(id = id, token = token)
    }

    override suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String,
        token: String
    ): UploadProductResult {
        return uploadProductDataSource.uploadProduct(
            file = file,
            productId = productId,
            token = token
        )
    }

    override suspend fun viewProductImage(
        productId: String,
        token: String
    ): ViewProductImageResult<List<ViewProductResponse>> {
        return viewProductImageDataSource.viewProductImage(productId = productId, token = token)
    }

    override suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        token: String
    ): ResetPasswordResult {
        return resetPasswordDataSource.resetPassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
            token = token
        )
    }

    override suspend fun logoutUser(
        email: String,
        uuid: String,
        token: String
    ): LogoutRemoteResult {
        return logoutDataSource.logoutUser(email = email, uuid = uuid, token = token)
    }

    override suspend fun generateQr(
        productId: String,
        token: String
    ): GenerateQrResult<GenerateQrResponse> {
        return remoteQrisDataSource.generateQr(productId, token)
    }
}
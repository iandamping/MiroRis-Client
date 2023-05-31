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
import okhttp3.MultipartBody
import java.io.File

interface RemoteDataSource {

    suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
    ): TokenRemoteResult<TokenResponse>

    suspend fun getFirebaseToken(): FirebaseIdResult<String>

    suspend fun userLogin(
        email: String,
        password: String,
        token: String,
        fcmId: String
    ): LoginRemoteResult<LoginResponse>

    suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult

    suspend fun getProfile(email: String, token: String): ProfileRemoteResult<ProfileResponse>

    suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String,
        token: String
    ): EditUserResult

    suspend fun refreshToken(
        email: String,
        token: String
    ): RefreshTokenRemoteResult<RefreshTokenResponse>

    suspend fun getBank(
        page: String,
        limit: String,
        bankName: String,
        token: String
    ): BanksResult<ListBankResponse>

    suspend fun getProductListUser(
        page: String,
        limit: String,
        token: String
    ): ProductListUserResult<ProductListUserResponse>

    suspend fun uploadUserImage(file: File?, email: String, token: String): UploadResult

    suspend fun getCategoryProduct(
        page: String,
        limit: String,
    ): CategoryProductResult<ListCategoryProductResponse>

    suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName:String,
        categoryId: String,
        productType: String,
    ): ProductCatalogResult<ListProductCatalogResponse>

    suspend fun productCreate(
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
    ) : ProductCreateResult


    suspend fun editProductCreate(
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
        token: String,
    ): EditProductCreateResult

    suspend fun deactivateProduct(
        id: String,
        productStatus: String,
        token: String
    ): DeactivateProductResult

    suspend fun getFunds(page:String, limit:String, token:String) : FundsResult<ListFundResponse>


    suspend fun getInvestments(
        page: String,
        limit: String,
        token: String
    ): InvestmentsResult<ListInvestmentResponse>

    suspend fun detailFund(id: String, token: String): DetailFundResult<DetailFundResponse>

    suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String,
        token: String
    ): UploadProductResult


    suspend fun viewProductImage(
        productId: String,
        token: String
    ): ViewProductImageResult<List<ViewProductResponse>>

    suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        token: String
    ): ResetPasswordResult

    suspend fun logoutUser(email: String, uuid: String, token: String): LogoutRemoteResult

    suspend fun generateQr(productId: String, token: String): GenerateQrResult<GenerateQrResponse>

}
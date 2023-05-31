package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.BankRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.CategoryRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.DeactivateProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.DetailFundRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.EditProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.EditUserRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.FundsRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.GenerateQrRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.InvestmentsRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.LogoutRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ProductCatalogRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ProductCreateRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ProductListUserRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ProfileRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.RegisterRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ResetPasswordRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.UploadProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.ViewProductRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DetailFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListFundResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListInvestmentResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProfileResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.RegisterErrorResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.CREATE_PRODUCT
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.DEACTIVATE_PRODUCT
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.DETAIL_FUND
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GENERATE_QR
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_BANK
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_CATEGORY_PRODUCT
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_PRODUCT_CATALOG
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_PRODUCT_USER
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_USER_PROFILE
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.LIST_FUND
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.LIST_INVESTMENT
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.LOGOUT_USER
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.REGISTER_USER
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.RESET_PASSWORD
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.UPDATE_PRODUCT
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.UPDATE_USER_PROFILE
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.UPLOAD_PRODUCT_USER
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.UPLOAD_USER_IMAGE
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.VIEW_PRODUCT_IMAGE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @POST(REGISTER_USER)
    suspend fun registerUser(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: RegisterRequest
    ): Response<BaseResponse<RegisterErrorResponse>>

    @POST(LOGOUT_USER)
    suspend fun logoutUser(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: LogoutRequest
    ): Response<VoidBaseResponse>

    @POST(RESET_PASSWORD)
    suspend fun resetPassword(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: ResetPasswordRequest
    ): Response<VoidBaseResponse>

    @POST(GET_USER_PROFILE)
    suspend fun getProfile(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: ProfileRequest
    ): Response<BaseResponse<ProfileResponse>>

    @POST(UPDATE_USER_PROFILE)
    suspend fun editUser(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: EditUserRequest
    ): Response<VoidBaseResponse>

    @POST(GET_BANK)
    suspend fun getBanks(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: BankRequest
    ): Response<BaseResponse<ListBankResponse>>

    @POST(GET_PRODUCT_USER)
    suspend fun getProductListUser(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: ProductListUserRequest
    ): Response<BaseResponse<ProductListUserResponse>>


    @POST(UPLOAD_USER_IMAGE)
    suspend fun uploadImageUser(
        @Header("Authorization") tokenAuthorization: String,
        @Body data: RequestBody
    ): Response<VoidBaseResponse>

    @POST(GET_CATEGORY_PRODUCT)
    suspend fun categoryProduct(
        @Header("Content-Type") contentType: String,
        @Body request: CategoryRequest
    ): Response<BaseResponse<ListCategoryProductResponse>>

    @POST(GET_PRODUCT_CATALOG)
    suspend fun productCatalog(
        @Header("Content-Type") contentType: String,
        @Body request: ProductCatalogRequest
    ): Response<BaseResponse<ListProductCatalogResponse>>

    @POST(CREATE_PRODUCT)
    suspend fun productCreate(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: ProductCreateRequest
    ): Response<VoidBaseResponse>

    @POST(UPDATE_PRODUCT)
    suspend fun editProductCreate(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: EditProductRequest
    ): Response<VoidBaseResponse>


    @POST(DEACTIVATE_PRODUCT)
    suspend fun deactivateProduct(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: DeactivateProductRequest
    ): Response<VoidBaseResponse>


    @POST(LIST_FUND)
    suspend fun getFunds(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: FundsRequest
    ): Response<BaseResponse<ListFundResponse>>


    @POST(LIST_INVESTMENT)
    suspend fun getInvestments(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: InvestmentsRequest
    ): Response<BaseResponse<ListInvestmentResponse>>


    @POST(DETAIL_FUND)
    suspend fun detailFund(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: DetailFundRequest
    ): Response<BaseResponse<DetailFundResponse>>


    @POST(UPLOAD_PRODUCT_USER)
    @Multipart
    suspend fun uploadProduct(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Part file: MultipartBody.Part,
        @Body request: UploadProductRequest
    ): Response<VoidBaseResponse>


    @POST(VIEW_PRODUCT_IMAGE)
    suspend fun viewProductImage(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: ViewProductRequest
    ): Response<BaseResponse<List<ViewProductResponse>>>


    @POST(GENERATE_QR)
    suspend fun generateQr(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: GenerateQrRequest
    ): Response<BaseResponse<GenerateQrResponse>>

    @POST(GENERATE_QR)
    suspend fun detailProduct(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: GenerateQrRequest
    ): Response<BaseResponse<GenerateQrResponse>>
}
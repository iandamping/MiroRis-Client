package com.spe.miroris.core.domain.repository

import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.domain.common.DomainProductResult
import com.spe.miroris.core.domain.common.DomainProductResultWithRefreshToken
import com.spe.miroris.core.domain.common.DomainVoidProductResult
import com.spe.miroris.core.domain.common.DomainVoidProductResultWithRefreshToken
import com.spe.miroris.core.domain.model.ListCategoryProduct
import com.spe.miroris.core.domain.model.ListProductCatalog
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ProductRepository {

    suspend fun getProductListUser(
        page: String,
        limit: String,
    ): DomainProductResultWithRefreshToken<ProductListUserResponse>

    suspend fun getCategoryProduct(
        page: String,
        limit: String,
    ): DomainProductResult<ListCategoryProduct>

    suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName: String,
        categoryId: String,
        productType: String,
    ): DomainProductResult<ListProductCatalog>

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
    ): DomainVoidProductResultWithRefreshToken

    suspend fun generateQr(productId: String): DomainProductResultWithRefreshToken<GenerateQrResponse>

    fun saveSelectedCategory(data: String)

    fun saveSelectedCategoryToDataStore(data: String)

    fun saveSelectedCategoryId(data: String)

    fun saveSelectedCategoryIdToDataStore(data: String)

    fun saveSelectedFunds(data: String)

    fun saveSelectedFundsToDataStore(data: String)

    fun getSelectedCategory():String

    fun getSelectedCategoryFlow(): Flow<String>

    fun getSelectedCategoryId():String

    fun getSelectedCategoryIdFlow(): Flow<String>

    fun getSelectedFund():String

    fun getSelectedFundFlow(): Flow<String>


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
    ): DomainVoidProductResultWithRefreshToken

    suspend fun deactivateProduct(
        id: String,
        productStatus: String,
    ): DomainVoidProductResult

    suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String,
    ): DomainVoidProductResult


    suspend fun viewProductImage(
        productId: String,
    ): DomainProductResult<List<ViewProductResponse>>

}
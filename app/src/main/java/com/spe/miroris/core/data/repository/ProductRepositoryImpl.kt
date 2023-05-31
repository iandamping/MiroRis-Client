package com.spe.miroris.core.data.repository

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.EditProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.GenerateQrResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductListUserResult
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ViewProductImageResult
import com.spe.miroris.core.data.dataSource.remote.model.response.GenerateQrResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.BASE_URL
import com.spe.miroris.core.domain.common.DomainProductResult
import com.spe.miroris.core.domain.common.DomainProductResultWithRefreshToken
import com.spe.miroris.core.domain.common.DomainVoidProductResult
import com.spe.miroris.core.domain.common.DomainVoidProductResultWithRefreshToken
import com.spe.miroris.core.domain.model.CategoryProduct
import com.spe.miroris.core.domain.model.ListCategoryProduct
import com.spe.miroris.core.domain.model.ListProductCatalog
import com.spe.miroris.core.domain.model.ProductCatalog
import com.spe.miroris.core.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cacheDataSource: CacheDataSource,
) : ProductRepository {

    companion object {
        private const val PRODUCT_TYPE_PROFIT = "Profit"
        private const val PRODUCT_TYPE_NON_PROFIT = "Non Profit"
    }

    override suspend fun getProductListUser(
        page: String,
        limit: String
    ): DomainProductResultWithRefreshToken<ProductListUserResponse> {
        return when (val remoteData = remoteDataSource.getProductListUser(
            page = page,
            limit = limit,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is ProductListUserResult.Error -> DomainProductResultWithRefreshToken.Error(remoteData.errorMessage)
            is ProductListUserResult.Success -> DomainProductResultWithRefreshToken.Success(
                remoteData.data
            )

            ProductListUserResult.RefreshToken -> DomainProductResultWithRefreshToken.RefreshToken
        }
    }

    override suspend fun getCategoryProduct(
        page: String,
        limit: String
    ): DomainProductResult<ListCategoryProduct> {
        return when (val remoteData = remoteDataSource.getCategoryProduct(
            page = page,
            limit = limit,
        )) {
            is CategoryProductResult.Error -> DomainProductResult.Error(remoteData.errorMessage)
            is CategoryProductResult.Success -> DomainProductResult.Success(
                ListCategoryProduct(
                    listOfCategoryProduct = remoteData.data.listOfCategoryProduct.map { mapData ->
                        CategoryProduct(
                            categoryId = mapData.categoryId ?: "",
                            categoryName = mapData.categoryName ?: ""
                        )
                    },
                    listOfFundType = listOf(PRODUCT_TYPE_PROFIT, PRODUCT_TYPE_NON_PROFIT)
                )
            )
        }
    }

    override suspend fun getProductCatalog(
        page: String,
        limit: String,
        productName:String,
        categoryId: String,
        productType: String,
    ): DomainProductResult<ListProductCatalog> {
        return when (val remoteData = remoteDataSource.getProductCatalog(
            page = page,
            limit = limit,
            productName = productName,
            categoryId = categoryId,
            productType = productType,
        )) {
            is ProductCatalogResult.Error -> DomainProductResult.Error(remoteData.errorMessage)
            is ProductCatalogResult.Success -> DomainProductResult.Success(
                ListProductCatalog(
                    remoteData.data.listOfProductCatalog.map { mapData ->
                        ProductCatalog(
                            productId = mapData.productId ?: "",
                            categoryId = mapData.categoryId ?: "",
                            userId = mapData.userId ?: "",
                            productName = mapData.productName ?: "",
                            productDetail = mapData.productDetail ?: "",
                            productDuration = mapData.productDuration ?: "",
                            productTypePayment = mapData.productTypePayment ?: "",
                            productType = mapData.productType ?: "",
                            productStartFunding = mapData.productStartFunding ?: "",
                            productFinishFunding = mapData.productFinishFunding ?: "",
                            productQris = mapData.productQris ?: "",
                            productPaid = mapData.productPaid ?: "",
                            productStatus = mapData.productStatus ?: "",
                            createdAt = mapData.createdAt ?: "",
                            updatedAt = mapData.updatedAt ?: "",
                            categoryName = mapData.categoryName ?: "",
                            productDurationName = mapData.productDurationName ?: "",
                            productTypePaymentName = mapData.productTypePaymentName ?: "",
                            productTypeName = mapData.productTypeName ?: "",
                            productImage = mapData.imagePaths.map { "$BASE_URL${it.productImage} " }
                        )
                    })
            )
        }
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
    ): DomainVoidProductResultWithRefreshToken {
        return when (val remoteData = remoteDataSource.productCreate(
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
            productStatus = productStatus,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}",
            personalAccount = personalAccount
        )) {
            is ProductCreateResult.Error -> DomainVoidProductResultWithRefreshToken.Error(remoteData.errorMessage)
            ProductCreateResult.Success -> DomainVoidProductResultWithRefreshToken.Success
            ProductCreateResult.RefreshToken -> DomainVoidProductResultWithRefreshToken.RefreshToken
        }
    }

    override suspend fun generateQr(productId: String): DomainProductResultWithRefreshToken<GenerateQrResponse> {
        return when (val remoteData = remoteDataSource.generateQr(
            productId = productId,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is GenerateQrResult.Error -> DomainProductResultWithRefreshToken.Error(remoteData.errorMessage)
            GenerateQrResult.RefreshToken -> DomainProductResultWithRefreshToken.RefreshToken
            is GenerateQrResult.Success -> DomainProductResultWithRefreshToken.Success(remoteData.data)
        }
    }

    override fun saveSelectedCategory(data: String) {
        cacheDataSource.saveSelectedCategory(data)
    }

    override fun saveSelectedCategoryToDataStore(data: String) {
        cacheDataSource.saveSelectedCategoryToDataStore(data)
    }

    override fun saveSelectedCategoryId(data: String) {
        cacheDataSource.saveSelectedCategoryId(data)
    }

    override fun saveSelectedCategoryIdToDataStore(data: String) {
        cacheDataSource.saveSelectedCategoryIdToDataStore(data)
    }

    override fun saveSelectedFunds(data: String) {
        cacheDataSource.saveSelectedFunds(data)
    }

    override fun saveSelectedFundsToDataStore(data: String) {
        cacheDataSource.saveSelectedFundsToDataStore(data)
    }

    override fun getSelectedCategory(): String {
        return cacheDataSource.getSelectedCategory()
    }

    override fun getSelectedCategoryFlow(): Flow<String> {
        return cacheDataSource.getSelectedCategoryFlow()
    }

    override fun getSelectedCategoryId(): String {
        return cacheDataSource.getSelectedCategoryId()
    }

    override fun getSelectedCategoryIdFlow(): Flow<String> {
        return cacheDataSource.getSelectedCategoryIdFlow()
    }

    override fun getSelectedFund(): String {
        return cacheDataSource.getSelectedFund()
    }

    override fun getSelectedFundFlow(): Flow<String> {
        return cacheDataSource.getSelectedFundFlow()
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
        productStatus: String
    ): DomainVoidProductResultWithRefreshToken {
        return when (val remoteData = remoteDataSource.editProductCreate(
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
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is EditProductCreateResult.Error -> DomainVoidProductResultWithRefreshToken.Error(
                remoteData.errorMessage
            )

            EditProductCreateResult.Success -> DomainVoidProductResultWithRefreshToken.Success
            EditProductCreateResult.RefreshToken -> DomainVoidProductResultWithRefreshToken.RefreshToken
        }
    }

    override suspend fun deactivateProduct(
        id: String,
        productStatus: String
    ): DomainVoidProductResult {
        return when (val remoteData = remoteDataSource.deactivateProduct(
            id = id, productStatus = productStatus,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is DeactivateProductResult.Error -> DomainVoidProductResult.Error(remoteData.errorMessage)
            DeactivateProductResult.Success -> DomainVoidProductResult.Success
        }
    }

    override suspend fun uploadProduct(
        file: MultipartBody.Part,
        productId: String
    ): DomainVoidProductResult {
        return when (val remoteData = remoteDataSource.uploadProduct(
            file = file,
            productId = productId,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is UploadProductResult.Error -> DomainVoidProductResult.Error(remoteData.errorMessage)
            UploadProductResult.Success -> DomainVoidProductResult.Success
        }
    }

    override suspend fun viewProductImage(productId: String): DomainProductResult<List<ViewProductResponse>> {
        return when (val remoteData = remoteDataSource.viewProductImage(
            productId = productId,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is ViewProductImageResult.Error -> DomainProductResult.Error(remoteData.errorMessage)
            is ViewProductImageResult.Success -> DomainProductResult.Success(remoteData.data)
        }
    }

}
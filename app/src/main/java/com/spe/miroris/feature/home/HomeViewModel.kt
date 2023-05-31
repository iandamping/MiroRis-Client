package com.spe.miroris.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.R
import com.spe.miroris.core.domain.common.DomainProductResult
import com.spe.miroris.core.domain.model.ProductCatalog
import com.spe.miroris.core.domain.repository.ProductRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.feature.home.banner.MultiAdapterProductBannerData
import com.spe.miroris.feature.home.common.HomeBanner
import com.spe.miroris.feature.home.common.HomeProduct
import com.spe.miroris.feature.home.product.EpoxyMultiProductHomeController.Companion.EMPTY_IMAGE
import com.spe.miroris.feature.home.product.MultiAdapterProductHomeData
import com.spe.miroris.feature.home.state.CategoryProductUiState
import com.spe.miroris.feature.home.state.ProductCatalogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userUseCase: UserUseCase
) :
    ViewModel() {

    private val _listBannerData: MutableStateFlow<MutableList<MultiAdapterProductBannerData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterProductBannerData.Shimmer(0),
                MultiAdapterProductBannerData.Shimmer(1),
                MultiAdapterProductBannerData.Shimmer(2),
            )
        )

    val listBannerData: StateFlow<List<MultiAdapterProductBannerData>> =
        _listBannerData.asStateFlow()

    private val _listProductData: MutableStateFlow<MutableList<MultiAdapterProductHomeData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterProductHomeData.Shimmer(0),
                MultiAdapterProductHomeData.Shimmer(1),
                MultiAdapterProductHomeData.Shimmer(2),
                MultiAdapterProductHomeData.Shimmer(3),
                MultiAdapterProductHomeData.Shimmer(4),
                MultiAdapterProductHomeData.Shimmer(5),
                MultiAdapterProductHomeData.Shimmer(6),
            )
        )
    val listProductData: StateFlow<List<MultiAdapterProductHomeData>> =
        _listProductData.asStateFlow()

    private val _categoryProductUiState: MutableStateFlow<CategoryProductUiState> =
        MutableStateFlow(
            CategoryProductUiState.initialize()
        )
    val categoryProductUiState: StateFlow<CategoryProductUiState> =
        _categoryProductUiState.asStateFlow()

    private val _productCatalogUiState: MutableStateFlow<ProductCatalogUiState> = MutableStateFlow(
        ProductCatalogUiState.initialize()
    )
    val productCatalogUiState: StateFlow<ProductCatalogUiState> =
        _productCatalogUiState.asStateFlow()


    init {
        viewModelScope.launch {
            when (val remoteData = productRepository.getCategoryProduct(page = "1", limit = "20")) {
                is DomainProductResult.Error -> _categoryProductUiState.update { currentUiState ->
                    currentUiState.copy(isLoading = false, errorMessage = remoteData.message)
                }

                is DomainProductResult.Success -> _categoryProductUiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        data = remoteData.data,
                    )
                }
            }
        }

        viewModelScope.launch {
            when (val remoteData =
                productRepository.getProductCatalog(
                    page = "1",
                    limit = "20",
                    productName = "",
                    categoryId = productRepository.getSelectedCategoryId(),
                    productType = productRepository.getSelectedFund()
                )) {
                is DomainProductResult.Error -> {
                    _productCatalogUiState.update { currentUiState ->
                        currentUiState.copy(
                            successState = ProductCatalogUiState.SuccessState.Failed,
                            errorMessage = remoteData.message
                        )
                    }
                }

                is DomainProductResult.Success -> {
                    _productCatalogUiState.update { currentUiState ->
                        currentUiState.copy(
                            data = remoteData.data.listOfProductCatalog,
                            successState = ProductCatalogUiState.SuccessState.Success
                        )
                    }
                }
            }
        }


        viewModelScope.launch {
            delay(2000)
            setEpoxyBanner()
        }
    }

    private fun setEpoxyBanner(
        dummyBannerData: List<HomeBanner> = listOf(
            HomeBanner(0, R.drawable.img_dummy_banner_1),
            HomeBanner(1, R.drawable.img_dummy_banner_1),
            HomeBanner(2, R.drawable.img_dummy_banner_1)
        )
    ) {
        _listBannerData.update {
            _listBannerData.value.toMutableList().apply {
                this.clear()
                dummyBannerData.forEach {
                    this.add(MultiAdapterProductBannerData.ProductBanner(it.epoxyId, it.imageId))
                }
            }
        }
    }

    suspend fun setEpoxyProduct(productData: List<ProductCatalog>) {
        val listOfProduct = withContext(Dispatchers.Default) {
            val listOfId = List(productData.size) { it + 1 }
            productData.zip(listOfId).map {
                HomeProduct(
                    epoxyId = it.second,
                    productId = it.first.productId,
                    categoryId = it.first.categoryId,
                    userId = it.first.userId,
                    productName = it.first.productName,
                    productDetail = it.first.productDetail,
                    productDuration = it.first.productDuration,
                    productTypePayment = it.first.productTypePayment,
                    productType = it.first.productType,
                    productStartFunding = it.first.productStartFunding,
                    productFinishFunding = it.first.productFinishFunding,
                    productQris = it.first.productQris,
                    productPaid = it.first.productPaid,
                    productStatus = it.first.productStatus,
                    createdAt = it.first.createdAt,
                    updatedAt = it.first.updatedAt,
                    categoryName = it.first.categoryName,
                    productDurationName = it.first.productDurationName,
                    productTypePaymentName = it.first.productTypePaymentName,
                    productTypeName = it.first.productTypeName,
                    productImage = it.first.productImage
                )
            }.toSet()
        }

        _listProductData.update {
            _listProductData.value.toMutableList().apply {
                this.clear()
                listOfProduct.forEach {
                    this.add(
                        MultiAdapterProductHomeData.ProductHome(
                            epoxyId = it.epoxyId,
                            productId = it.productId,
                            categoryId = it.categoryId,
                            userId = it.userId,
                            productName = it.productName,
                            productDetail = it.productDetail,
                            productDuration = it.productDuration,
                            productTypePayment = it.productTypePayment,
                            productType = it.productType,
                            productStartFunding = it.productStartFunding,
                            productFinishFunding = it.productFinishFunding,
                            productQris = it.productQris,
                            productPaid = it.productPaid,
                            productStatus = it.productStatus,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                            categoryName = it.categoryName,
                            productDurationName = it.productDurationName,
                            productTypePaymentName = it.productTypePaymentName,
                            productTypeName = it.productTypeName,
                            productImage = if (it.productImage.isNotEmpty()) it.productImage.first() else EMPTY_IMAGE,
                            randomRating = (0..500).random()
                        )
                    )
                }
            }
        }
    }

    fun setErrorEpoxyProduct() {
        _listProductData.value = mutableListOf(MultiAdapterProductHomeData.Error)
    }

    fun getProductCatalog(page: String, limit: String, productName:String,categoryId: String, productType: String) {
        viewModelScope.launch {
            when (val remoteData =
                productRepository.getProductCatalog(
                    page = page,
                    limit = limit,
                    productName = productName,
                    categoryId = categoryId,
                    productType = productType
                )) {
                is DomainProductResult.Error -> {
                    _productCatalogUiState.update { currentUiState ->
                        currentUiState.copy(
                            successState = ProductCatalogUiState.SuccessState.Failed, errorMessage = remoteData.message
                        )
                    }
                }

                is DomainProductResult.Success -> {
                    _productCatalogUiState.update { currentUiState ->
                        currentUiState.copy(
                            successState = ProductCatalogUiState.SuccessState.Success,
                            data = remoteData.data.listOfProductCatalog
                        )
                    }
                }
            }
        }
    }

    fun saveSelectedCategory(data: String) = productRepository.saveSelectedCategory(data)

    fun saveSelectedCategoryToDataStore(data: String) {
        productRepository.saveSelectedCategoryToDataStore(data)
    }

    fun saveSelectedCategoryId(data: String) = productRepository.saveSelectedCategoryId(data)

    fun saveSelectedCategoryIdToDataStore(data: String) {
        productRepository.saveSelectedCategoryIdToDataStore(data)
    }

    fun saveSelectedFunds(data: String) = productRepository.saveSelectedFunds(data)

    fun saveSelectedFundsToDataStore(data: String) {
        productRepository.saveSelectedFundsToDataStore(data)
    }

    fun getSelectedCategory(): String = productRepository.getSelectedCategory()

    fun getSelectedCategoryFlow(): Flow<String> = productRepository.getSelectedCategoryFlow()

    fun getSelectedCategoryId(): String = productRepository.getSelectedCategoryId()

    fun getSelectedCategoryIdFlow(): Flow<String> = productRepository.getSelectedCategoryIdFlow()

    fun getSelectedFund(): String = productRepository.getSelectedFund()

    fun getSelectedFundFlow(): Flow<String> = productRepository.getSelectedFundFlow()

    fun getUserEmail(): String = userUseCase.getUserEmail()
}
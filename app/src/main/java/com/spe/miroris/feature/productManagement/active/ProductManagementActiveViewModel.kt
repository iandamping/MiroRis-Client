package com.spe.miroris.feature.productManagement.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainProductResultWithRefreshToken
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.ProductRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductManagementActiveViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _productManagementUiState: MutableStateFlow<ProductManagementActiveUiState> =
        MutableStateFlow(
            ProductManagementActiveUiState.initialize()
        )
    val productManagementUiState: StateFlow<ProductManagementActiveUiState> =
        _productManagementUiState.asStateFlow()

    private val _activeProductUser: MutableStateFlow<MutableList<MultiAdapterProductManagementActiveData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterProductManagementActiveData.Shimmer(0),
                MultiAdapterProductManagementActiveData.Shimmer(1),
                MultiAdapterProductManagementActiveData.Shimmer(2),
            )
        )
    val activeProductUser: StateFlow<List<MultiAdapterProductManagementActiveData>> =
        _activeProductUser.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenProductManagementActiveUiState> =
        MutableStateFlow(
            RefreshTokenProductManagementActiveUiState.initialize()
        )
    val refreshTokenUiState: StateFlow<RefreshTokenProductManagementActiveUiState> =
        _refreshTokenUiState.asStateFlow()

    init {
        getProductData()
    }

    fun getProductData() {
        viewModelScope.launch {
            when (val remoteData = productRepository.getProductListUser("1", "20")) {
                is DomainProductResultWithRefreshToken.Error -> {
                    _productManagementUiState.update { currentUiState ->
                        currentUiState.copy(
                            errorMessage = remoteData.message,
                            successState = ProductManagementActiveUiState.SuccessState.Failed
                        )
                    }
                }

                DomainProductResultWithRefreshToken.RefreshToken -> _productManagementUiState.update { currentUiState ->
                    currentUiState.copy(successState = ProductManagementActiveUiState.SuccessState.RefreshToken)
                }

                is DomainProductResultWithRefreshToken.Success -> {
                    _productManagementUiState.update { currentUiState ->
                        currentUiState.copy(
                            successState = ProductManagementActiveUiState.SuccessState.Success,
                            data = remoteData.data.listOfProduct
                        )
                    }
                }
            }
        }
    }

    fun getUserEmail() = userUseCase.getUserEmail()


    fun refreshToken(email: String) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = RefreshTokenProductManagementActiveUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenProductManagementActiveUiState.SuccessState.Success
                    )
                }
            }
        }
    }

    fun setupFailedProductActive() {
        _activeProductUser.value =
            mutableListOf(MultiAdapterProductManagementActiveData.Error)
    }


    suspend fun setupSuccessProductActive(data: List<ProductUserResponse>) {
        if (data.isEmpty()) {
            _activeProductUser.value =
                mutableListOf(MultiAdapterProductManagementActiveData.Error)
        } else {
            val listInvestment = withContext(Dispatchers.Default) {
                val listOfId = List(data.size) { it + 1 }
                data.zip(listOfId).map {
                    ProductActiveData(
                        epoxyId = it.second,
                        productId = it.first.productId ?: "",
                        categoryId = it.first.categoryId ?: "",
                        userId = it.first.userId ?: "",
                        productName = it.first.productName ?: "",
                        productDetail = it.first.productDetail ?: "",
                        productDuration = it.first.productDuration ?: "",
                        productTypePayment = it.first.productTypePayment ?: "",
                        productType = it.first.productType ?: "",
                        productStartFunding = it.first.productStartFunding ?: "",
                        productFinishFunding = it.first.productFinishFunding ?: "",
                        productQris = it.first.productQris ?: "",
                        productPaid = it.first.productPaid ?: "",
                        productStatus = it.first.productStatus ?: "",
                        createdAt = it.first.createdAt ?: "",
                        updatedAt = it.first.updatedAt ?: "",
                        categoryName = it.first.categoryName ?: "",
                        productDurationName = it.first.productDurationName ?: "",
                        productTypePaymentName = it.first.productTypePaymentName ?: "",
                        productTypeName = it.first.productTypeName ?: "",
                        address = it.first.address ?: "",
                        fundingStatus = it.first.fundingStatus ?: "",
                        productImage = it.first.productImage ?: ""
                    )
                }.toMutableList()
            }

            _activeProductUser.update {
                _activeProductUser.value.toMutableList().apply {
                    this.clear()
                    listInvestment.forEach {
                        if (it.fundingStatus == "1") {
                            this.add(
                                MultiAdapterProductManagementActiveData.ProductActive(
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
                                    address = it.address,
                                    productImage = it.productImage
                                )
                            )

                        }
                    }
                }
            }
        }
    }
}
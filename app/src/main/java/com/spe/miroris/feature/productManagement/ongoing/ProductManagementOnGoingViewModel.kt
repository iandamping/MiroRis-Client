package com.spe.miroris.feature.productManagement.ongoing

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
class ProductManagementOnGoingViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _productManagementUiState: MutableStateFlow<ProductManagementOnGoingUiState> =
        MutableStateFlow(
            ProductManagementOnGoingUiState.initialize()
        )
    val productManagementUiState: StateFlow<ProductManagementOnGoingUiState> =
        _productManagementUiState.asStateFlow()

    private val _onGoingProductUser: MutableStateFlow<MutableList<MultiAdapterProductManagementOnGoingData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterProductManagementOnGoingData.Shimmer(0),
                MultiAdapterProductManagementOnGoingData.Shimmer(1),
                MultiAdapterProductManagementOnGoingData.Shimmer(2),
            )
        )
    val onGoingProductUser: StateFlow<List<MultiAdapterProductManagementOnGoingData>> =
        _onGoingProductUser.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenProductManagementOnGoingUiState> =
        MutableStateFlow(
            RefreshTokenProductManagementOnGoingUiState.initialize()
        )
    val refreshTokenUiState: StateFlow<RefreshTokenProductManagementOnGoingUiState> =
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
                            successState = ProductManagementOnGoingUiState.SuccessState.Failed
                        )
                    }
                }

                DomainProductResultWithRefreshToken.RefreshToken -> _productManagementUiState.update { currentUiState ->
                    currentUiState.copy(successState = ProductManagementOnGoingUiState.SuccessState.RefreshToken)
                }

                is DomainProductResultWithRefreshToken.Success -> {
                    _productManagementUiState.update { currentUiState ->
                        currentUiState.copy(
                            successState = ProductManagementOnGoingUiState.SuccessState.Success,
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
                        successState = RefreshTokenProductManagementOnGoingUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenProductManagementOnGoingUiState.SuccessState.Success
                    )
                }
            }
        }
    }

    fun setupFailedProductOnGoing() {
        _onGoingProductUser.value =
            mutableListOf(MultiAdapterProductManagementOnGoingData.Error)
    }


    suspend fun setupSuccessProductOnGoing(data: List<ProductUserResponse>) {
        if (data.isEmpty()) {
            _onGoingProductUser.value =
                mutableListOf(MultiAdapterProductManagementOnGoingData.Error)
        } else {
            val listInvestment = withContext(Dispatchers.Default) {
                val listOfId = List(data.size) { it + 1 }
                data.zip(listOfId).map {
                    ProductOnGoingData(
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

            _onGoingProductUser.update {
                _onGoingProductUser.value.toMutableList().apply {
                    this.clear()
                    listInvestment.forEach {
                        if (it.fundingStatus == "2"){
                            this.add(
                                MultiAdapterProductManagementOnGoingData.ProductOnGoing(
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
package com.spe.miroris.feature.editProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainVoidProductResultWithRefreshToken
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.ProductRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.feature.addProduct.RefreshTokenAddProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProductViewModelSecondStep @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {


    private val _editProductUiState: MutableStateFlow<EditCreateProductUiState> =
        MutableStateFlow(EditCreateProductUiState.initialize())
    val editProductUiState: StateFlow<EditCreateProductUiState> =
        _editProductUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenAddProductUiState> =
        MutableStateFlow(RefreshTokenAddProductUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenAddProductUiState> =
        _refreshTokenUiState.asStateFlow()

    fun getUserEmail(): String = userUseCase.getUserEmail()


    fun createProduct(
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
    ) {
        viewModelScope.launch {
            when (val bankData = productRepository.productCreate(
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
                productStatus = productStatus
            )) {
                is DomainVoidProductResultWithRefreshToken.Error -> _editProductUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = EditCreateProductUiState.SuccessState.Error,
                        errorMessage = bankData.message
                    )
                }

                DomainVoidProductResultWithRefreshToken.RefreshToken -> _editProductUiState.update { currentUiState ->
                    currentUiState.copy(successState = EditCreateProductUiState.SuccessState.RefreshToken)
                }

                is DomainVoidProductResultWithRefreshToken.Success -> _editProductUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = EditCreateProductUiState.SuccessState.Success,
                    )
                }
            }
        }
    }


    fun refreshToken(email: String) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        errorMessage = remoteData.message,
                        successState = RefreshTokenAddProductUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        successState = RefreshTokenAddProductUiState.SuccessState.Success
                    )
                }
            }
        }
    }
}
package com.spe.miroris.feature.addProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.common.DomainProductResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.InvestmentRepository
import com.spe.miroris.core.domain.repository.ProductRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.feature.home.state.CategoryProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModelFirstStep @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository,
    private val investmentRepository: InvestmentRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _bankUiState: MutableStateFlow<AddProductBankUiState> =
        MutableStateFlow(AddProductBankUiState.initialize())
    val bankUiState: StateFlow<AddProductBankUiState> = _bankUiState.asStateFlow()

    private val _categoryProductUiState: MutableStateFlow<CategoryProductUiState> =
        MutableStateFlow(
            CategoryProductUiState.initialize()
        )
    val categoryProductUiState: StateFlow<CategoryProductUiState> =
        _categoryProductUiState.asStateFlow()


    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenAddProductUiState> =
        MutableStateFlow(RefreshTokenAddProductUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenAddProductUiState> =
        _refreshTokenUiState.asStateFlow()

    init {
        getBankData()

        getCategoryProduct()
    }

    fun getCategoryProduct(){
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
    }

    fun getUserEmail(): String = userUseCase.getUserEmail()

    fun getPhoneNumber(): String = userUseCase.getPhoneNumber()

    fun getBankCode(): String = userUseCase.getBankCode()

    fun getBankName(): String = userUseCase.getBankName()

    fun getAccountNumber(): String = userUseCase.getAccountNumber()


    fun getBankData() {
        viewModelScope.launch {
            when (val bankData = investmentRepository.getBank("1", "20", "")) {
                is DomainInvestmentResult.Error -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = AddProductBankUiState.SuccessState.Error,
                        errorMessage = bankData.message
                    )
                }

                DomainInvestmentResult.RefreshToken -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(successState = AddProductBankUiState.SuccessState.RefreshToken)
                }

                is DomainInvestmentResult.Success -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = AddProductBankUiState.SuccessState.Success,
                        data = bankData.data.listOfBank
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
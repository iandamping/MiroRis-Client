package com.spe.miroris.feature.fundManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.data.dataSource.remote.model.response.FundResponse
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.InvestmentRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.feature.fundManagement.ongoing.FundOnGoing
import com.spe.miroris.feature.fundManagement.ongoing.MultiAdapterFundOnGoingData
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
class FundManagementViewModel @Inject constructor(
    private val investmentRepository: InvestmentRepository,
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _fundOnGoing: MutableStateFlow<MutableList<MultiAdapterFundOnGoingData>> =
        MutableStateFlow(
            mutableListOf(
                MultiAdapterFundOnGoingData.Shimmer(0),
                MultiAdapterFundOnGoingData.Shimmer(1),
                MultiAdapterFundOnGoingData.Shimmer(2),
            )
        )
    val fundOnGoing: StateFlow<List<MultiAdapterFundOnGoingData>> =
        _fundOnGoing.asStateFlow()


    private val _fundManagementUiState: MutableStateFlow<FundManagementUiState> = MutableStateFlow(
        FundManagementUiState.initialize()
    )
    val fundManagementUiState: StateFlow<FundManagementUiState> =
        _fundManagementUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenFundManagementUiState> =
        MutableStateFlow(
            RefreshTokenFundManagementUiState.initialize()
        )
    val refreshTokenUiState: StateFlow<RefreshTokenFundManagementUiState> =
        _refreshTokenUiState.asStateFlow()

    init {
        getFund()
    }

    fun getFund() {
        viewModelScope.launch {
            when (val remoteData = investmentRepository.getFunds("1", "20")) {
                is DomainInvestmentResult.Error -> _fundManagementUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = FundManagementUiState.SuccessState.Failed
                    )
                }

                DomainInvestmentResult.RefreshToken -> _fundManagementUiState.update { currentUiState ->
                    currentUiState.copy(successState = FundManagementUiState.SuccessState.RefreshToken)
                }

                is DomainInvestmentResult.Success -> _fundManagementUiState.update { currentUiState ->
                    currentUiState.copy(
                        totalAmount = remoteData.data.totalAmount,
                        data = remoteData.data.listOfFund,
                        successState = FundManagementUiState.SuccessState.Success
                    )
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
                        successState = RefreshTokenFundManagementUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenFundManagementUiState.SuccessState.Success
                    )
                }
            }
        }
    }

    fun setupFailedInvestmentOnGoing() {
        _fundOnGoing.value =
            mutableListOf(MultiAdapterFundOnGoingData.Error)
    }


    suspend fun setupSuccessInvestmentOnGoing(data: List<FundResponse>) {
        if (data.isEmpty()) {
            _fundOnGoing.value =
                mutableListOf(MultiAdapterFundOnGoingData.Error)
        } else {
            val listInvestment = withContext(Dispatchers.Default) {
                val listOfId = List(data.size) { it + 1 }
                data.zip(listOfId).map {
                    FundOnGoing(
                        epoxyId = it.second,
                        categoryId = it.first.categoryId ?: "",
                        productName = it.first.productName ?: "",
                        productDetail = it.first.productDetail ?: "",
                        productStatus = it.first.productStatus ?: "",
                        categoryName = it.first.categoryName ?: "",
                        fundId = it.first.fundId ?: "",
                        paymentAmount = it.first.paymentAmount
                    )
                }.toMutableList()
            }

            _fundOnGoing.update {
                _fundOnGoing.value.toMutableList().apply {
                    this.clear()
                    listInvestment.forEach {
                        this.add(
                            MultiAdapterFundOnGoingData.Fund(
                                epoxyId = it.epoxyId,
                                categoryId = it.categoryId,
                                productName = it.productName,
                                productDetail = it.productDetail,
                                productStatus = it.productStatus,
                                categoryName = it.categoryName,
                                fundId = it.fundId,
                                paymentAmount = it.paymentAmount
                            )
                        )

                    }
                }
            }
        }
    }
}
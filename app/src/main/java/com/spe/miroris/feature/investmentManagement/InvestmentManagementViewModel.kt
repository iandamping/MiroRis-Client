package com.spe.miroris.feature.investmentManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.InvestmentRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvestmentManagementViewModel @Inject constructor(
    private val investmentRepository: InvestmentRepository,
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _investmentManagementUiState: MutableStateFlow<InvestmentManagementUiState> =
        MutableStateFlow(
            InvestmentManagementUiState.initialize()
        )
    val investmentManagementUiState: StateFlow<InvestmentManagementUiState> =
        _investmentManagementUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenInvestmentManagementUiState> =
        MutableStateFlow(
            RefreshTokenInvestmentManagementUiState.initialize()
        )
    val refreshTokenUiState: StateFlow<RefreshTokenInvestmentManagementUiState> =
        _refreshTokenUiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val remoteData = investmentRepository.getInvestments("1", "20")) {
                is DomainInvestmentResult.Error -> _investmentManagementUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = InvestmentManagementUiState.SuccessState.Failed
                    )
                }

                DomainInvestmentResult.RefreshToken -> _investmentManagementUiState.update { currentUiState ->
                    currentUiState.copy(successState = InvestmentManagementUiState.SuccessState.RefreshToken)
                }

                is DomainInvestmentResult.Success -> _investmentManagementUiState.update { currentUiState ->
                    currentUiState.copy(
                        data = remoteData.data.listOfInvestment,
                        totalAmount = remoteData.data.totalAmount,
                        successState = InvestmentManagementUiState.SuccessState.Success
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
                        successState = RefreshTokenInvestmentManagementUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenInvestmentManagementUiState.SuccessState.Success
                    )
                }
            }
        }
    }
}
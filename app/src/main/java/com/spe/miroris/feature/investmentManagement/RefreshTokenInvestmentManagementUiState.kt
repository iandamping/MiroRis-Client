package com.spe.miroris.feature.investmentManagement

data class RefreshTokenInvestmentManagementUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenInvestmentManagementUiState {
            return RefreshTokenInvestmentManagementUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}

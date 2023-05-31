package com.spe.miroris.feature.fundManagement

data class RefreshTokenFundManagementUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenFundManagementUiState {
            return RefreshTokenFundManagementUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}


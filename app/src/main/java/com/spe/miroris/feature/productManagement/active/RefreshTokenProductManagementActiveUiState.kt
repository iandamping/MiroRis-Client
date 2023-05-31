package com.spe.miroris.feature.productManagement.active

data class RefreshTokenProductManagementActiveUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenProductManagementActiveUiState {
            return RefreshTokenProductManagementActiveUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
package com.spe.miroris.feature.productManagement.ongoing

data class RefreshTokenProductManagementOnGoingUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenProductManagementOnGoingUiState {
            return RefreshTokenProductManagementOnGoingUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
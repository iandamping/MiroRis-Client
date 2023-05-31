package com.spe.miroris.feature.productManagement.done

data class RefreshTokenProductManagementDoneUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenProductManagementDoneUiState {
            return RefreshTokenProductManagementDoneUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
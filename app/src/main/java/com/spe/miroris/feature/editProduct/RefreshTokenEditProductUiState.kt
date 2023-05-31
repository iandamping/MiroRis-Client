package com.spe.miroris.feature.editProduct

data class RefreshTokenEditProductUiState(
    val isLoading: Boolean,
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenEditProductUiState {
            return RefreshTokenEditProductUiState(
                isLoading = true,
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
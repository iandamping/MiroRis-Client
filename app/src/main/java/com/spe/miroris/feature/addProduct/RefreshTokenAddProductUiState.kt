package com.spe.miroris.feature.addProduct

data class RefreshTokenAddProductUiState(
    val isLoading: Boolean,
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenAddProductUiState {
            return RefreshTokenAddProductUiState(
                isLoading = true,
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
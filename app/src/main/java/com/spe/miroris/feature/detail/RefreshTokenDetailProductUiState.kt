package com.spe.miroris.feature.detail

data class RefreshTokenDetailProductUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenDetailProductUiState {
            return RefreshTokenDetailProductUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
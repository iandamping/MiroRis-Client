package com.spe.miroris.feature.token

data class TokenUiState(
    val isSuccess: Boolean,
    val errorMessage: String
) {
    companion object {
        fun initialize(): TokenUiState {
            return TokenUiState(isSuccess = false, errorMessage = "")
        }
    }
}
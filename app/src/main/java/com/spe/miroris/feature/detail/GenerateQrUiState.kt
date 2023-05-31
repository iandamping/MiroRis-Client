package com.spe.miroris.feature.detail

data class GenerateQrUiState(
    val successState: SuccessState,
    val errorMessage: String,
    val data: String
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = GenerateQrUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
            data = ""
        )
    }
}

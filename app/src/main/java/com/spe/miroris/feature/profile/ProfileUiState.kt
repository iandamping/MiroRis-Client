package com.spe.miroris.feature.profile


data class ProfileUiState(
    val successState: SuccessState,
    val errorMessage: String,
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = ProfileUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
        )
    }
}

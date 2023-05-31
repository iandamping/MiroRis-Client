package com.spe.miroris.feature.profile

data class LogoutUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): LogoutUiState {
            return LogoutUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}

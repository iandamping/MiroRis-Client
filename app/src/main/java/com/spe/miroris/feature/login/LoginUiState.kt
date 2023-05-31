package com.spe.miroris.feature.login

data class LoginUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): LoginUiState {
            return LoginUiState(errorMessage = "", SuccessState.Initialize)
        }
    }
}

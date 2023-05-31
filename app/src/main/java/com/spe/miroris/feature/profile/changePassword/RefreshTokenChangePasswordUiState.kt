package com.spe.miroris.feature.profile.changePassword

data class RefreshTokenChangePasswordUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize(): RefreshTokenChangePasswordUiState {
            return RefreshTokenChangePasswordUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}

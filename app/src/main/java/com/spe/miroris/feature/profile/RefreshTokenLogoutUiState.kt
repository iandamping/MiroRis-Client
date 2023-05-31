package com.spe.miroris.feature.profile

data class RefreshTokenLogoutUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, SuccessProfile, SuccessLogout, Failed
    }

    companion object {
        fun initialize(): RefreshTokenLogoutUiState {
            return RefreshTokenLogoutUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}

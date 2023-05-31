package com.spe.miroris.feature.profile.changePassword

data class ChangePasswordUiState(
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): ChangePasswordUiState {
            return ChangePasswordUiState(
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}

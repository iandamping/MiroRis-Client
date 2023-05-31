package com.spe.miroris.feature.profile.edit


data class RefreshTokenEditUiState(
    val isLoading: Boolean,
    val errorMessage: String,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, SuccessForBank, SuccessForEditUser, Failed
    }

    companion object {
        fun initialize(): RefreshTokenEditUiState {
            return RefreshTokenEditUiState(
                isLoading = true,
                errorMessage = "",
                successState = SuccessState.Initialize
            )
        }
    }
}
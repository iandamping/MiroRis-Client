package com.spe.miroris.feature.registration

data class RegistrationUiState(
    val successState: SuccessState,
    val errorMessage: String,
) {

    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): RegistrationUiState {
            return RegistrationUiState(
                successState = SuccessState.Initialize,
                errorMessage = ""
            )
        }
    }
}
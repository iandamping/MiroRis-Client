package com.spe.miroris.feature.addProduct

data class AddCreateProductUiState(
    val successState: SuccessState,
    val errorMessage: String,
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = AddCreateProductUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
        )
    }
}

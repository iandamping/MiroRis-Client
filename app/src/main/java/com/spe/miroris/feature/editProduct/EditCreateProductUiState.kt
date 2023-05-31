package com.spe.miroris.feature.editProduct

data class EditCreateProductUiState(
    val successState: SuccessState,
    val errorMessage: String,
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = EditCreateProductUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
        )
    }
}

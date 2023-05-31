package com.spe.miroris.feature.home.state

import com.spe.miroris.core.domain.model.ProductCatalog

data class GenerateQrUiState(
    val errorMessage: String,
    val successState: SuccessState,
    val data: List<ProductCatalog>
) {
    enum class SuccessState {
        Initialize, Success, Failed
    }

    companion object {
        fun initialize() = GenerateQrUiState(
            errorMessage = "",
            successState = SuccessState.Initialize,
            data = emptyList()
        )
    }
}

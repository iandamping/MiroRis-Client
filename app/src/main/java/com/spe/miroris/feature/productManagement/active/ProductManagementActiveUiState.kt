package com.spe.miroris.feature.productManagement.active

import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse

data class ProductManagementActiveUiState(
    val errorMessage: String,
    val data: List<ProductUserResponse>,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): ProductManagementActiveUiState {
            return ProductManagementActiveUiState(
                errorMessage = "",
                successState = SuccessState.Initialize,
                data = emptyList()
            )
        }
    }
}
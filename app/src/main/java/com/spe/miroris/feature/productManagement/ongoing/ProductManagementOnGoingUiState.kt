package com.spe.miroris.feature.productManagement.ongoing

import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse

data class ProductManagementOnGoingUiState(
    val errorMessage: String,
    val data: List<ProductUserResponse>,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): ProductManagementOnGoingUiState {
            return ProductManagementOnGoingUiState(
                errorMessage = "",
                successState = SuccessState.Initialize,
                data = emptyList()
            )
        }
    }
}
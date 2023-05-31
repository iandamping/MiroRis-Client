package com.spe.miroris.feature.productManagement.done

import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse

data class ProductManagementDoneUiState(
    val errorMessage: String,
    val data: List<ProductUserResponse>,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): ProductManagementDoneUiState {
            return ProductManagementDoneUiState(
                errorMessage = "",
                successState = SuccessState.Initialize,
                data = emptyList()
            )
        }
    }
}
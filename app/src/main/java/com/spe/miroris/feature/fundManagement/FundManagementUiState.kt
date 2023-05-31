package com.spe.miroris.feature.fundManagement

import com.spe.miroris.core.data.dataSource.remote.model.response.FundResponse

data class FundManagementUiState(
    val errorMessage: String,
    val successState: SuccessState,
    val totalAmount: String,
    val data: List<FundResponse>
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): FundManagementUiState {
            return FundManagementUiState(
                errorMessage = "",
                successState = SuccessState.Initialize,
                totalAmount = "",
                data = emptyList()
            )
        }
    }
}
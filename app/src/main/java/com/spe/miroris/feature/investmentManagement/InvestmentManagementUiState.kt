package com.spe.miroris.feature.investmentManagement

import com.spe.miroris.core.data.dataSource.remote.model.response.InvestmentResponse

data class InvestmentManagementUiState(
    val errorMessage: String,
    val totalAmount:String,
    val data:List<InvestmentResponse>,
    val successState: SuccessState
) {
    enum class SuccessState {
        Initialize, Success, Failed, RefreshToken
    }

    companion object {
        fun initialize(): InvestmentManagementUiState {
            return InvestmentManagementUiState(
                errorMessage = "",
                successState = SuccessState.Initialize,
                totalAmount = "",
                data = emptyList()
            )
        }
    }
}

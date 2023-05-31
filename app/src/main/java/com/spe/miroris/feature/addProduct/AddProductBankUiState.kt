package com.spe.miroris.feature.addProduct

import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse

data class AddProductBankUiState(
    val successState: SuccessState,
    val errorMessage: String,
    val data: List<BankResponse>
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = AddProductBankUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
            data = emptyList()
        )
    }
}

package com.spe.miroris.feature.editProduct

import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse

data class EditProductBankUiState(
    val successState: SuccessState,
    val errorMessage: String,
    val data: List<BankResponse>
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = EditProductBankUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
            data = emptyList()
        )
    }
}

package com.spe.miroris.feature.profile.edit

import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse

data class BankUiState(
    val successState: SuccessState,
    val errorMessage: String,
    val data: List<BankResponse>
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = BankUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
            data = emptyList()
        )
    }
}

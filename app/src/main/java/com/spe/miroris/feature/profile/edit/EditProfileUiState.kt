package com.spe.miroris.feature.profile.edit

import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse


data class EditProfileUiState(
    val successState: SuccessState,
    val errorMessage: String,
    val data: List<BankResponse>
) {
    enum class SuccessState {
        Initialize, Success, Error, RefreshToken
    }

    companion object {
        fun initialize() = EditProfileUiState(
            successState = SuccessState.Initialize,
            errorMessage = "",
            data = emptyList()
        )
    }
}

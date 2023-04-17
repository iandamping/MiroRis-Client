package com.spe.miroris.feature.home.state

import com.spe.miroris.feature.home.banner.DummyBanner

data class BannerUiState(
    val isLoading: Boolean,
    val data: List<DummyBanner>,
    val errorMessage: String
) {
    companion object {
        fun initialize(): BannerUiState {
            return BannerUiState(isLoading = true, data = emptyList(), errorMessage = "")
        }
    }
}

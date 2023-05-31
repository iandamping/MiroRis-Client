package com.spe.miroris.feature.home.state

import com.spe.miroris.core.domain.model.ListCategoryProduct

data class CategoryProductUiState(
    val isLoading: Boolean,
    val errorMessage: String,
    val data: ListCategoryProduct?,
) {
    companion object {
        fun initialize(): CategoryProductUiState {
            return CategoryProductUiState(
                isLoading = true,
                errorMessage = "",
                data = null
            )
        }
    }
}

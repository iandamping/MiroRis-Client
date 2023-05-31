package com.spe.miroris.core.domain.model

data class ListCategoryProduct(
    val listOfCategoryProduct: List<CategoryProduct>,
    val listOfFundType: List<String>
)
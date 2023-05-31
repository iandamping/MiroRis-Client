package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ProductCatalogResult <out T>{

    data class Success<out T>(val data: T) : ProductCatalogResult<T>()

    data class Error(val errorMessage: String) : ProductCatalogResult<Nothing>()
}
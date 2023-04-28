package com.spe.miroris.core.data.dataSource.remote.model

interface ResponseStatusCode {
    fun getCode(): String
}

enum class ResponseStatus : ResponseStatusCode {

    Success {
        override fun getCode(): String {
            return "000"
        }
    },

}
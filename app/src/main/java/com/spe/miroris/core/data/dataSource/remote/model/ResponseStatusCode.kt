package com.spe.miroris.core.data.dataSource.remote.model

interface ResponseStatusCode {
    fun getCode(): Int
}

enum class ResponseStatus : ResponseStatusCode {

    Success {
        override fun getCode(): Int {
            return 200
        }
    },

}
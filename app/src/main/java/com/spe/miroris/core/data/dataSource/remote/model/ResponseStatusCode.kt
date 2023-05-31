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
    LoginOnUse {
        override fun getCode(): Int {
            return 308
        }
    },
    RefreshToken {
        override fun getCode(): Int {
            return 401
        }

    }

}
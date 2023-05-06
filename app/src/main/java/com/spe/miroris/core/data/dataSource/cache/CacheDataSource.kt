package com.spe.miroris.core.data.dataSource.cache

interface CacheDataSource {

    fun saveUserToken(data: String)

    fun getUserToken(): String

    fun getDeviceID(): String
}
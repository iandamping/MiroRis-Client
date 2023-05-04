package com.spe.miroris.core.data.dataSource.cache

import com.spe.miroris.core.data.dataSource.cache.preference.PreferenceHelper
import java.util.UUID
import javax.inject.Inject

class CacheDataSourceImpl @Inject constructor(private val preferenceHelper: PreferenceHelper) :
    CacheDataSource {
    companion object {
        private const val SAVE_USER_KEY = "save user"
        private const val CACHE_UUID_KEY = "cache uuid user"
    }

    override fun saveUserToken(data: String) {
        preferenceHelper.saveStringInSharedPreference(SAVE_USER_KEY, data)
    }

    override fun getUserToken(): String {
        return preferenceHelper.getStringInSharedPreference(SAVE_USER_KEY)
    }

    override fun getDeviceID(): String {
        return if (preferenceHelper.getStringInSharedPreference(CACHE_UUID_KEY) != "") {
            preferenceHelper.getStringInSharedPreference(CACHE_UUID_KEY)
        } else {
            val randomValue = UUID.randomUUID().toString().replace("-", "")
            preferenceHelper.saveStringInSharedPreference(CACHE_UUID_KEY, randomValue)

            preferenceHelper.getStringInSharedPreference(CACHE_UUID_KEY)
        }
    }

}
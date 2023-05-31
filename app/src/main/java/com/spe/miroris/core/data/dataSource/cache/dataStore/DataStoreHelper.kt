package com.spe.miroris.core.data.dataSource.cache.dataStore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreHelper {

    suspend fun saveStringInDataStore(key: Preferences.Key<String>, value: String)

    fun getStringInDataStore(key: Preferences.Key<String>): Flow<String>

    suspend fun saveIntInDataStore(key: Preferences.Key<Int>, value: Int)

    fun getIntInDataStore(key: Preferences.Key<Int>): Flow<Int>

}

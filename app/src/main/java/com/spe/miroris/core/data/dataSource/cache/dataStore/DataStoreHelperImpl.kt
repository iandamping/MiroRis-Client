package com.spe.miroris.core.data.dataSource.cache.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelperImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    DataStoreHelper {

    override suspend fun saveStringInDataStore(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun getStringInDataStore(key: Preferences.Key<String>): Flow<String> {
        return dataStore.data.map { preferences -> preferences[key] ?: "" }
    }

    override suspend fun saveIntInDataStore(key: Preferences.Key<Int>, value: Int) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun getIntInDataStore(key: Preferences.Key<Int>): Flow<Int> {
        return dataStore.data.map { preferences -> preferences[key] ?: 0 }
    }
}

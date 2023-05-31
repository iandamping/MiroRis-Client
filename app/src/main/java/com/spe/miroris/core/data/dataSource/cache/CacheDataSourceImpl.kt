package com.spe.miroris.core.data.dataSource.cache

import androidx.datastore.preferences.core.stringPreferencesKey
import com.spe.miroris.core.data.dataSource.cache.dataStore.DataStoreHelper
import com.spe.miroris.core.data.dataSource.cache.preference.PreferenceHelper
import com.spe.miroris.di.qualifier.CustomNonDispatcherScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class CacheDataSourceImpl @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val dataStoreHelper: DataStoreHelper,
    @CustomNonDispatcherScope private val scope:CoroutineScope
) :
    CacheDataSource {
    companion object {
        private const val AUTH_TOKEN_KEY = "save token AUTH"
        private const val USER_TOKEN_KEY = "save token user"
        private const val USER_BEARER_KEY = "save bearer user"
        private const val USER_EMAIL_KEY = "save email"
        private val USER_EMAIL_DATASTORE_KEY = stringPreferencesKey("save email flow")
        private val USER_NAME_DATASTORE_KEY = stringPreferencesKey("save username flow")
        private val USER_PROFILE_IMAGE_DATASTORE_KEY =
            stringPreferencesKey("save profile image flow")
        private val USER_CATEGORY_DATASTORE_KEY = stringPreferencesKey("save user category flow")
        private val USER_CATEGORY_ID_DATASTORE_KEY =
            stringPreferencesKey("save user category id flow")
        private val USER_FUNDS_DATASTORE_KEY = stringPreferencesKey("save user funds id flow")
        private const val SAVE_USER_ADDRESS_KEY = "save address"
        private const val USER_CITY_KEY = "save city"
        private const val USER_PROFILE_IMAGE_KEY = "save profile image"
        private const val USERNAME_KEY = "save username"
        private const val CATEGORY_KEY = "save category"
        private const val CATEGORY_ID_KEY = "save id category"
        private const val FUND_KEY = "save fund"
        private const val CACHE_UUID_KEY = "cache uuid user"
        private const val USER_BANK_CODE_KEY = "bank CODE user"
        private const val USER_BANK_NAME_KEY = "BANK NAME user"
        private const val USER_ACCOUNT_NUMBER_KEY = "ACCOUNT NUMBER user"
        private const val USER_PHONE_NUMBER_KEY = "ACCOUNT PHONE NUMBER user"
    }

    override fun saveAuthToken(data: String) {
        preferenceHelper.saveStringInSharedPreference(AUTH_TOKEN_KEY,data)
    }

    override fun saveUserToken(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_TOKEN_KEY, data)
    }

    override fun saveUserBearer(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_BEARER_KEY, data)
    }

    override fun saveUserName(data: String) {
        preferenceHelper.saveStringInSharedPreference(USERNAME_KEY,data)
    }

    override fun saveUserNameToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_NAME_DATASTORE_KEY, data)
        }
    }

    override fun saveUserAddress(data: String) {
        preferenceHelper.saveStringInSharedPreference(SAVE_USER_ADDRESS_KEY, data)
    }

    override fun saveUserCity(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_CITY_KEY, data)
    }

    override fun saveUserEmail(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_EMAIL_KEY, data)
    }

    override fun saveBankCode(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_BANK_CODE_KEY,data)
    }

    override fun saveAccountNumber(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_ACCOUNT_NUMBER_KEY,data)

    }

    override fun savePhoneNumber(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_PHONE_NUMBER_KEY,data)
    }

    override fun saveBankName(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_BANK_NAME_KEY,data)

    }

    override fun saveUserEmailToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_EMAIL_DATASTORE_KEY, data)
        }
    }

    override fun saveUserImageProfile(data: String) {
        preferenceHelper.saveStringInSharedPreference(USER_PROFILE_IMAGE_KEY, data)
    }

    override fun saveUserImageProfileToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_PROFILE_IMAGE_DATASTORE_KEY, data)
        }
    }

    override fun saveSelectedCategory(data: String) {
        preferenceHelper.saveStringInSharedPreference(CATEGORY_KEY, data)
    }

    override fun saveSelectedCategoryToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_CATEGORY_DATASTORE_KEY, data)
        }
    }

    override fun saveSelectedCategoryId(data: String) {
        preferenceHelper.saveStringInSharedPreference(CATEGORY_ID_KEY, data)
    }

    override fun saveSelectedCategoryIdToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_CATEGORY_ID_DATASTORE_KEY, data)
        }
    }

    override fun saveSelectedFunds(data: String) {
        preferenceHelper.saveStringInSharedPreference(FUND_KEY, data)
    }

    override fun saveSelectedFundsToDataStore(data: String) {
        scope.launch {
            dataStoreHelper.saveStringInDataStore(USER_FUNDS_DATASTORE_KEY, data)
        }
    }

    override fun getAuthToken(): String {
        return preferenceHelper.getStringInSharedPreference(AUTH_TOKEN_KEY)
    }

    override fun getUserToken(): String {
        return preferenceHelper.getStringInSharedPreference(USER_TOKEN_KEY)
    }

    override fun getUserBearer(): String {
        return preferenceHelper.getStringInSharedPreference(USER_BEARER_KEY)
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

    override fun getUserEmail(): String {
        return preferenceHelper.getStringInSharedPreference(USER_EMAIL_KEY)
    }

    override fun getUserEmailFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_EMAIL_DATASTORE_KEY)
    }

    override fun getUserNameFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_NAME_DATASTORE_KEY)

    }

    override fun getUserName(): String {
        return preferenceHelper.getStringInSharedPreference(USERNAME_KEY)
    }

    override fun getUserAddress(): String {
        return preferenceHelper.getStringInSharedPreference(SAVE_USER_ADDRESS_KEY)
    }

    override fun getUserCity(): String {
        return preferenceHelper.getStringInSharedPreference(USER_CITY_KEY)
    }

    override fun getUserImageProfile(): String {
        return preferenceHelper.getStringInSharedPreference(USER_PROFILE_IMAGE_KEY)
    }

    override fun getUserImageProfileFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_PROFILE_IMAGE_DATASTORE_KEY)

    }

    override fun getSelectedCategory(): String {
        return preferenceHelper.getStringInSharedPreference(CATEGORY_KEY)
    }

    override fun getSelectedCategoryFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_CATEGORY_DATASTORE_KEY)
    }

    override fun getSelectedCategoryId(): String {
        return preferenceHelper.getStringInSharedPreference(CATEGORY_ID_KEY)
    }

    override fun getSelectedCategoryIdFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_CATEGORY_ID_DATASTORE_KEY)
    }

    override fun getSelectedFund(): String {
        return preferenceHelper.getStringInSharedPreference(FUND_KEY)
    }

    override fun getSelectedFundFlow(): Flow<String> {
        return dataStoreHelper.getStringInDataStore(USER_FUNDS_DATASTORE_KEY)
    }

    override fun getBankCode(): String {
        return preferenceHelper.getStringInSharedPreference(USER_BANK_CODE_KEY)
    }

    override fun getAccountNumber(): String {
        return preferenceHelper.getStringInSharedPreference(USER_ACCOUNT_NUMBER_KEY)
    }

    override fun getBankName(): String {
        return preferenceHelper.getStringInSharedPreference(USER_BANK_NAME_KEY)

    }

    override fun getPhoneNumber(): String {
        return preferenceHelper.getStringInSharedPreference(USER_PHONE_NUMBER_KEY)
    }

    override fun deleteUserRelatedData() {
        with(preferenceHelper) {
            deleteSharedPreference(USERNAME_KEY)
            deleteSharedPreference(USER_CITY_KEY)
            deleteSharedPreference(USER_EMAIL_KEY)
            deleteSharedPreference(USER_PROFILE_IMAGE_KEY)
            deleteSharedPreference(USER_TOKEN_KEY)
            deleteSharedPreference(SAVE_USER_ADDRESS_KEY)
            deleteSharedPreference(FUND_KEY)
            deleteSharedPreference(CATEGORY_KEY)
            deleteSharedPreference(CATEGORY_ID_KEY)
            deleteSharedPreference(USER_BANK_CODE_KEY)
            deleteSharedPreference(USER_BANK_NAME_KEY)
            deleteSharedPreference(USER_ACCOUNT_NUMBER_KEY)
            deleteSharedPreference(USER_PHONE_NUMBER_KEY)
        }

    }

    override fun deleteUserRelatedDataInDataStore() {
        scope.launch {
            with(dataStoreHelper){
                saveStringInDataStore(USER_NAME_DATASTORE_KEY, "")
                saveStringInDataStore(USER_EMAIL_DATASTORE_KEY, "")
                saveStringInDataStore(USER_PROFILE_IMAGE_DATASTORE_KEY, "")
                saveStringInDataStore(USER_FUNDS_DATASTORE_KEY, "")
                saveStringInDataStore(USER_CATEGORY_ID_DATASTORE_KEY, "")
                saveStringInDataStore(USER_CATEGORY_DATASTORE_KEY, "")
            }
        }

    }

}
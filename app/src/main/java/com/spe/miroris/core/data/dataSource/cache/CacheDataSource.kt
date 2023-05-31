package com.spe.miroris.core.data.dataSource.cache

import kotlinx.coroutines.flow.Flow

interface CacheDataSource {

    fun saveAuthToken(data: String)

    fun saveUserToken(data: String)

    fun saveUserBearer(data: String)

    fun saveUserName(data: String)

    fun saveUserNameToDataStore(data: String)

    fun saveUserAddress(data: String)

    fun saveUserCity(data: String)

    fun saveUserEmail(data: String)

    fun saveBankCode(data: String)

    fun saveAccountNumber(data: String)

    fun savePhoneNumber(data: String)

    fun saveBankName(data: String)

    fun saveUserEmailToDataStore(data: String)

    fun saveUserImageProfile(data: String)

    fun saveUserImageProfileToDataStore(data: String)

    fun saveSelectedCategory(data: String)

    fun saveSelectedCategoryToDataStore(data: String)

    fun saveSelectedCategoryId(data: String)

    fun saveSelectedCategoryIdToDataStore(data: String)

    fun saveSelectedFunds(data: String)

    fun saveSelectedFundsToDataStore(data: String)

    fun getAuthToken(): String

    fun getUserToken(): String

    fun getUserBearer(): String

    fun getDeviceID(): String

    fun getUserEmail(): String

    fun getUserEmailFlow(): Flow<String>

    fun getUserNameFlow(): Flow<String>

    fun getUserName(): String

    fun getUserAddress(): String

    fun getUserCity(): String

    fun getUserImageProfile(): String

    fun getUserImageProfileFlow(): Flow<String>

    fun getSelectedCategory():String

    fun getSelectedCategoryFlow(): Flow<String>

    fun getSelectedCategoryId(): String

    fun getSelectedCategoryIdFlow(): Flow<String>

    fun getSelectedFund(): String

    fun getSelectedFundFlow(): Flow<String>

    fun getBankCode(): String

    fun getAccountNumber(): String

    fun getBankName(): String

    fun getPhoneNumber(): String

    fun deleteUserRelatedData()

    fun deleteUserRelatedDataInDataStore()

}
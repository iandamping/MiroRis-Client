package com.spe.miroris.core.domain.repository

import com.spe.miroris.core.domain.common.DomainLoginResult
import com.spe.miroris.core.domain.common.DomainLoginWithRefreshTokenResult
import com.spe.miroris.core.domain.common.DomainUserResponseResult
import com.spe.miroris.core.domain.common.DomainVoidLoginResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    suspend fun getFirebaseToken(): DomainUserResponseResult<String>

    suspend fun userLogin(
        email: String,
        password: String,
        fcmId: String
    ): DomainLoginResult<String>

    suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ): DomainVoidLoginResult

    suspend fun getProfile(email: String): DomainLoginWithRefreshTokenResult

    suspend fun uploadUserImage(
        file: File?,
        email: String
    ): DomainLoginWithRefreshTokenResult

    suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String
    ): DomainLoginWithRefreshTokenResult


    suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): DomainLoginWithRefreshTokenResult


    suspend fun logoutUser(email: String): DomainLoginWithRefreshTokenResult

    fun getUserEmail(): String

    fun getUserEmailFlow(): Flow<String>

    fun getUserName(): String

    fun getUserNameFlow(): Flow<String>

    fun getUserAddress(): String

    fun getUserCity(): String

    fun getUserImageProfileFlow(): Flow<String>

    fun getUserImageProfile(): String

    fun getBankCode():String

    fun getAccountNumber(): String

    fun getBankName(): String

    fun getPhoneNumber(): String

}
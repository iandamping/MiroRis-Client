package com.spe.miroris.core.domain.useCase

import com.spe.miroris.core.domain.useCase.model.UseCaseProfileResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserUseCase {

    suspend fun userLogin(email: String, password: String): UseCaseProfileResult

    suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ): UseCaseProfileResult


    suspend fun getProfile(email: String): UseCaseProfileResult


    suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String,
        file: File?,
    ): UseCaseProfileResult


    suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): UseCaseProfileResult


    suspend fun logoutUser(email: String): UseCaseProfileResult

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
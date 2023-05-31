package com.spe.miroris.core.data.repository

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.EditUserResult
import com.spe.miroris.core.data.dataSource.remote.model.common.FirebaseIdResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.LogoutRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProfileRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ResetPasswordResult
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadResult
import com.spe.miroris.core.domain.common.DomainLoginResult
import com.spe.miroris.core.domain.common.DomainLoginWithRefreshTokenResult
import com.spe.miroris.core.domain.common.DomainUserResponseResult
import com.spe.miroris.core.domain.common.DomainVoidLoginResult
import com.spe.miroris.core.domain.repository.UserRepository
import com.spe.miroris.core.presentation.helper.UtilityHelper
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cacheDataSource: CacheDataSource,
    private val utilityHelperImpl: UtilityHelper
) : UserRepository {
    override suspend fun getFirebaseToken(): DomainUserResponseResult<String> {
        return when (val data = remoteDataSource.getFirebaseToken()) {
            is FirebaseIdResult.Error -> DomainUserResponseResult.Error(
                utilityHelperImpl.getString(
                    R.string.encryption_error_message
                )
            )

            is FirebaseIdResult.Success -> DomainUserResponseResult.Success(data.data)
        }
    }

    override suspend fun userLogin(
        email: String,
        password: String,
        fcmId: String
    ): DomainLoginResult<String> {
        return when (val remoteData =
            remoteDataSource.userLogin(
                email = email,
                password = password,
                fcmId = fcmId,
                token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getAuthToken()}"
            )) {
            LoginRemoteResult.EncryptionError -> DomainLoginResult.Error(
                utilityHelperImpl.getString(
                    R.string.encryption_error_message
                )
            )

            is LoginRemoteResult.Error -> DomainLoginResult.Error(remoteData.errorMessage)
            is LoginRemoteResult.Success -> {
                with(cacheDataSource) {
                    saveUserToken(remoteData.data.userToken)
                    saveUserEmail(remoteData.data.email)
                    saveUserEmailToDataStore(remoteData.data.email)
                }
                DomainLoginResult.Success(remoteData.data.email)
            }
        }
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ): DomainVoidLoginResult {
        return when (val remoteData =
            remoteDataSource.registerUser(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getAuthToken()}"
            )) {
            is RegisterRemoteResult.Error -> DomainVoidLoginResult.Error(remoteData.errorMessage)
            RegisterRemoteResult.Success -> DomainVoidLoginResult.Success
        }
    }

    override suspend fun getProfile(email: String): DomainLoginWithRefreshTokenResult {
        return when (val remoteData = remoteDataSource.getProfile(
            email = email,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is ProfileRemoteResult.Error -> DomainLoginWithRefreshTokenResult.Error(remoteData.errorMessage)
            is ProfileRemoteResult.Success -> {
                with(cacheDataSource) {
                    if (remoteData.data.phoneNumber != null) {
                        savePhoneNumber(remoteData.data.phoneNumber)
                    }
                    if (remoteData.data.bankCode != null) {
                        saveBankCode(remoteData.data.bankCode)
                    }
                    if (remoteData.data.namaBank != null) {
                        saveBankName(remoteData.data.namaBank)
                    }
                    if (remoteData.data.accountNumber != null) {
                        saveAccountNumber(remoteData.data.accountNumber)
                    }
                    if (remoteData.data.email != null) {
                        saveUserEmail(remoteData.data.email)
                        saveUserEmailToDataStore(remoteData.data.email)
                    }
                    if (remoteData.data.address != null) {
                        saveUserAddress(remoteData.data.address)
                    }
                    if (remoteData.data.username != null) {
                        saveUserName(remoteData.data.username)
                        saveUserNameToDataStore(remoteData.data.username)
                    }
                    if (remoteData.data.picture != null) {
                        saveUserImageProfileToDataStore(remoteData.data.picture)
                        saveUserImageProfile(remoteData.data.picture)
                    }
                    if (remoteData.data.city != null) {
                        saveUserCity(remoteData.data.city)
                    }
                }
                DomainLoginWithRefreshTokenResult.Success
            }

            ProfileRemoteResult.RefreshToken -> DomainLoginWithRefreshTokenResult.RefreshToken
        }
    }

    override suspend fun uploadUserImage(
        file: File?,
        email: String
    ): DomainLoginWithRefreshTokenResult {
        return when (val remoteData = remoteDataSource.uploadUserImage(
            file = file,
            email = email,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is UploadResult.Error -> DomainLoginWithRefreshTokenResult.Error(remoteData.errorMessage)
            UploadResult.Success -> DomainLoginWithRefreshTokenResult.Success
        }
    }

    override suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String
    ): DomainLoginWithRefreshTokenResult {
        return when (val remoteData = remoteDataSource.editUser(
            username = username,
            email = email,
            address = address,
            phoneNumber = phoneNumber,
            city = city,
            bankCode = bankCode,
            accountNumber = accountNumber,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is EditUserResult.Error -> DomainLoginWithRefreshTokenResult.Error(remoteData.errorMessage)
            EditUserResult.Success -> DomainLoginWithRefreshTokenResult.Success
        }
    }

    override suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): DomainLoginWithRefreshTokenResult {
        return when (val remoteData = remoteDataSource.resetPassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is ResetPasswordResult.Error -> DomainLoginWithRefreshTokenResult.Error(remoteData.errorMessage)
            ResetPasswordResult.Success -> DomainLoginWithRefreshTokenResult.Success
            ResetPasswordResult.RefreshToken -> DomainLoginWithRefreshTokenResult.RefreshToken
        }
    }

    override suspend fun logoutUser(
        email: String,
    ): DomainLoginWithRefreshTokenResult {
        return when (val remoteData = remoteDataSource.logoutUser(
            email = email,
            uuid = cacheDataSource.getDeviceID(),
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getUserToken()}"
        )) {
            is LogoutRemoteResult.Error -> DomainLoginWithRefreshTokenResult.Error(remoteData.errorMessage)
            LogoutRemoteResult.Success -> {
                cacheDataSource.deleteUserRelatedData()
                cacheDataSource.deleteUserRelatedDataInDataStore()
                DomainLoginWithRefreshTokenResult.Success
            }

            LogoutRemoteResult.RefreshToken -> DomainLoginWithRefreshTokenResult.RefreshToken
        }
    }

    override fun getUserEmail(): String {
        return cacheDataSource.getUserEmail()
    }

    override fun getUserEmailFlow(): Flow<String> {
        return cacheDataSource.getUserEmailFlow()
    }

    override fun getUserName(): String {
        return cacheDataSource.getUserName()
    }

    override fun getUserNameFlow(): Flow<String> {
        return cacheDataSource.getUserNameFlow()
    }

    override fun getUserAddress(): String {
        return cacheDataSource.getUserAddress()
    }

    override fun getUserCity(): String {
        return cacheDataSource.getUserCity()
    }

    override fun getUserImageProfileFlow(): Flow<String> {
        return cacheDataSource.getUserImageProfileFlow()
    }

    override fun getUserImageProfile(): String {
        return cacheDataSource.getUserImageProfile()
    }

    override fun getBankCode(): String {
        return cacheDataSource.getBankCode()
    }

    override fun getAccountNumber(): String {
        return cacheDataSource.getAccountNumber()
    }

    override fun getBankName(): String {
        return cacheDataSource.getBankName()
    }

    override fun getPhoneNumber(): String {
        return cacheDataSource.getPhoneNumber()
    }


}
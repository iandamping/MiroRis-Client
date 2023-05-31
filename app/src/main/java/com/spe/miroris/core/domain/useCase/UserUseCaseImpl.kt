package com.spe.miroris.core.domain.useCase

import com.spe.miroris.core.domain.common.DomainLoginResult
import com.spe.miroris.core.domain.common.DomainLoginWithRefreshTokenResult
import com.spe.miroris.core.domain.common.DomainUserResponseResult
import com.spe.miroris.core.domain.common.DomainVoidLoginResult
import com.spe.miroris.core.domain.repository.UserRepository
import com.spe.miroris.core.domain.useCase.model.UseCaseProfileResult
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : UserUseCase {
    override suspend fun userLogin(
        email: String,
        password: String,
    ): UseCaseProfileResult {
        return when (val firebaseData = userRepository.getFirebaseToken()) {
            is DomainUserResponseResult.Error -> UseCaseProfileResult.Error(firebaseData.message)
            is DomainUserResponseResult.Success -> {
                when (val userLoginData = userRepository.userLogin(
                        email = email,
                        password = password,
                        fcmId = firebaseData.data
                    )) {
                    is DomainLoginResult.Error -> UseCaseProfileResult.Error(userLoginData.message)
                    is DomainLoginResult.Success -> {
                        when (val userProfileData = userRepository.getProfile(userLoginData.data)) {
                            is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(
                                userProfileData.message
                            )

                            DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
                            DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
                        }
                    }
                }
            }
        }
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ): UseCaseProfileResult {
        return when (val registerData =
            userRepository.registerUser(email, password, confirmPassword)) {
            is DomainVoidLoginResult.Error -> UseCaseProfileResult.Error(registerData.message)
            DomainVoidLoginResult.Success -> {
                when (val firebaseData = userRepository.getFirebaseToken()) {
                    is DomainUserResponseResult.Error -> UseCaseProfileResult.Error(firebaseData.message)
                    is DomainUserResponseResult.Success -> {
                        when (val userLoginData =
                            userRepository.userLogin(
                                email = email,
                                password = password,
                                fcmId = firebaseData.data
                            )) {
                            is DomainLoginResult.Error -> UseCaseProfileResult.Error(userLoginData.message)
                            is DomainLoginResult.Success -> {
                                when (val userProfileData =
                                    userRepository.getProfile(userLoginData.data)) {
                                    is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(
                                        userProfileData.message
                                    )

                                    DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
                                    DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun getProfile(email: String): UseCaseProfileResult {
        return when (val logoutData = userRepository.getProfile(email)) {
            is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(logoutData.message)
            DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
            DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
        }
    }

    override suspend fun editUser(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String,
        file: File?,
    ): UseCaseProfileResult {
        return when (val editUser = userRepository.editUser(
            username = username,
            email = email,
            address = address,
            phoneNumber = phoneNumber,
            city = city,
            bankCode = bankCode,
            accountNumber = accountNumber
        )) {
            is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(editUser.message)
            DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
            DomainLoginWithRefreshTokenResult.Success -> {
                if (file == null) {
                    UseCaseProfileResult.Success
                } else {
                    when (val uploadImage =
                        userRepository.uploadUserImage(file = file, email = email)) {
                        is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(
                            uploadImage.message
                        )

                        DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
                        DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
                    }
                }

            }
        }
    }

    override suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): UseCaseProfileResult {
        return when (val resetData = userRepository.resetPassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        )) {
            is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(resetData.message)
            DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
            DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
        }
    }

    override suspend fun logoutUser(email: String): UseCaseProfileResult {
        return when (val logoutData = userRepository.logoutUser(email)) {
            is DomainLoginWithRefreshTokenResult.Error -> UseCaseProfileResult.Error(logoutData.message)
            DomainLoginWithRefreshTokenResult.RefreshToken -> UseCaseProfileResult.RefreshToken
            DomainLoginWithRefreshTokenResult.Success -> UseCaseProfileResult.Success
        }
    }

    override fun getUserEmail(): String {
        return userRepository.getUserEmail()
    }

    override fun getUserEmailFlow(): Flow<String> {
        return userRepository.getUserEmailFlow()
    }

    override fun getUserName(): String {
        return userRepository.getUserName()
    }

    override fun getUserNameFlow(): Flow<String> {
        return userRepository.getUserNameFlow()
    }

    override fun getUserAddress(): String {
        return userRepository.getUserAddress()
    }

    override fun getUserCity(): String {
        return userRepository.getUserCity()
    }

    override fun getUserImageProfileFlow(): Flow<String> {
        return userRepository.getUserImageProfileFlow()
    }

    override fun getUserImageProfile(): String {
        return userRepository.getUserImageProfile()
    }

    override fun getBankCode(): String {
        return userRepository.getBankCode()
    }

    override fun getAccountNumber(): String {
        return userRepository.getAccountNumber()
    }

    override fun getBankName(): String {
        return userRepository.getBankName()
    }

    override fun getPhoneNumber(): String {
        return userRepository.getPhoneNumber()
    }
}
package com.spe.miroris.feature.profile.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainInvestmentResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.InvestmentRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.core.domain.useCase.model.UseCaseProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authRepository: AuthRepository,
    private val investmentRepository: InvestmentRepository,
) : ViewModel() {
    private val _passedUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val passedUri: StateFlow<Uri?> = _passedUri.asStateFlow()

    private val _bankUiState: MutableStateFlow<BankUiState> =
        MutableStateFlow(BankUiState.initialize())
    val bankUiState: StateFlow<BankUiState> = _bankUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenEditUiState> =
        MutableStateFlow(RefreshTokenEditUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenEditUiState> =
        _refreshTokenUiState.asStateFlow()

    private val _editProfileUiState: MutableStateFlow<EditProfileUiState> =
        MutableStateFlow(EditProfileUiState.initialize())
    val editProfileUiState: StateFlow<EditProfileUiState> = _editProfileUiState.asStateFlow()


    val emailFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getUserEmail())
    val userNameFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getUserName())
    val addressFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getUserAddress())
    val phoneNumberFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getPhoneNumber())
    val cityFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getUserCity())
    val accountNumberFlow: MutableStateFlow<String> = MutableStateFlow(userUseCase.getAccountNumber())


    init {
        getBankData()
    }

    fun getDefaultImage() = userUseCase.getUserImageProfile()
    fun getBankName() = userUseCase.getBankName()

    fun getBankData() {
        viewModelScope.launch {
            when (val bankData = investmentRepository.getBank("1", "20", "")) {
                is DomainInvestmentResult.Error -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = BankUiState.SuccessState.Error,
                        errorMessage = bankData.message
                    )
                }

                DomainInvestmentResult.RefreshToken -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(successState = BankUiState.SuccessState.RefreshToken)
                }

                is DomainInvestmentResult.Success -> _bankUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = BankUiState.SuccessState.Success,
                        data = bankData.data.listOfBank
                    )
                }
            }
        }
    }

    fun editProfile(
        username: String,
        email: String,
        address: String,
        phoneNumber: String,
        city: String,
        bankCode: String,
        accountNumber: String,
        file: File?,
    ) {
        viewModelScope.launch {
            when (val editData = userUseCase.editUser(
                username = username,
                email = email,
                address = address,
                phoneNumber = phoneNumber,
                city = city,
                bankCode = bankCode,
                accountNumber = accountNumber,
                file = file
            )) {
                is UseCaseProfileResult.Error -> _editProfileUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = editData.message,
                        successState = EditProfileUiState.SuccessState.Error
                    )
                }

                UseCaseProfileResult.RefreshToken -> _editProfileUiState.update { currentUiState ->
                    currentUiState.copy(successState = EditProfileUiState.SuccessState.RefreshToken)
                }

                UseCaseProfileResult.Success -> _editProfileUiState.update { currentUiState ->
                    currentUiState.copy(successState = EditProfileUiState.SuccessState.Success)
                }
            }
        }
    }

    fun setPassedUri(uri: Uri?) {
        _passedUri.value = uri
    }

    fun getUserEmail(): String = userUseCase.getUserEmail()

    fun refreshToken(email: String, call: EditProfileCaller) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        errorMessage = remoteData.message,
                        successState = RefreshTokenEditUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        successState = when (call) {
                            EditProfileCaller.Bank -> RefreshTokenEditUiState.SuccessState.SuccessForBank
                            EditProfileCaller.EditUser -> RefreshTokenEditUiState.SuccessState.SuccessForEditUser
                        }
                    )
                }
            }
        }
    }


    fun setEmailManually(data: String) {
        emailFlow.value = data
    }

    fun setUsernameManually(data: String) {
        userNameFlow.value = data
    }

    fun setAddressManually(data: String) {
        addressFlow.value = data
    }

    fun setPhoneNumberManually(data: String) {
        phoneNumberFlow.value = data
    }


    fun setCityManually(data: String) {
        cityFlow.value = data
    }


    fun setAccountNumberManually(data: String) {
        accountNumberFlow.value = data
    }

    fun resetState() {
        _bankUiState.value = BankUiState.initialize()
        _refreshTokenUiState.value = RefreshTokenEditUiState.initialize()
        _editProfileUiState.value = EditProfileUiState.initialize()
    }

}
package com.spe.miroris.feature.profile.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.core.domain.useCase.model.UseCaseProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val useCase: UserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _changePasswordUiState: MutableStateFlow<ChangePasswordUiState> = MutableStateFlow(
        ChangePasswordUiState.initialize()
    )
    val changePasswordUiState: StateFlow<ChangePasswordUiState> =
        _changePasswordUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenChangePasswordUiState> =
        MutableStateFlow(RefreshTokenChangePasswordUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenChangePasswordUiState> =
        _refreshTokenUiState.asStateFlow()

    val currentPasswordFlow: MutableStateFlow<String> = MutableStateFlow("")
    val newPasswordFlow: MutableStateFlow<String> = MutableStateFlow("")
    val confirmPasswordFlow: MutableStateFlow<String> = MutableStateFlow("")

    fun getUserEmail() = useCase.getUserEmail()

    fun setCurrentPasswordManually(data: String) {
        currentPasswordFlow.value = data
    }

    fun setNewPasswordManually(data: String) {
        newPasswordFlow.value = data
    }

    fun setConfirmPasswordManually(data: String) {
        confirmPasswordFlow.value = data
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            when (val remoteData =
                useCase.resetPassword(currentPassword, newPassword, confirmPassword)) {
                is UseCaseProfileResult.Error -> _changePasswordUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = ChangePasswordUiState.SuccessState.Failed
                    )
                }

                UseCaseProfileResult.RefreshToken -> _changePasswordUiState.update { currentUiState ->
                    currentUiState.copy(successState = ChangePasswordUiState.SuccessState.RefreshToken)
                }

                UseCaseProfileResult.Success -> _changePasswordUiState.update { currentUiState ->
                    currentUiState.copy(successState = ChangePasswordUiState.SuccessState.Success)
                }
            }
        }
    }

    fun refreshToken(email: String) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = RefreshTokenChangePasswordUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenChangePasswordUiState.SuccessState.Success
                    )
                }
            }
        }
    }

}
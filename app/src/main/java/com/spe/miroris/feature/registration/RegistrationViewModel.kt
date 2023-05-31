package com.spe.miroris.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RegistrationViewModel @Inject constructor(private val userUseCase: UserUseCase) :
    ViewModel() {

    val emailFlow: MutableStateFlow<String> = MutableStateFlow("")
    val passwordFlow: MutableStateFlow<String> = MutableStateFlow("")
    val confirmPasswordFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val _registrationUiState: MutableStateFlow<RegistrationUiState> =
        MutableStateFlow(RegistrationUiState.initialize())
    val registrationUiState: StateFlow<RegistrationUiState> = _registrationUiState.asStateFlow()

    fun setEmailManually(data: String) {
        emailFlow.value = data
    }

    fun setPasswordManually(data: String) {
        passwordFlow.value = data
    }

    fun setConfirmPasswordManually(data: String) {
        confirmPasswordFlow.value = data
    }


    fun register(email: String, password: String, cfmPassword: String) {
        viewModelScope.launch {
            when (val registData = userUseCase.registerUser(
                email = email,
                password = password,
                confirmPassword = cfmPassword,
            )) {

                is UseCaseProfileResult.Error -> _registrationUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = registData.message,
                        successState = RegistrationUiState.SuccessState.Failed
                    )
                }

                UseCaseProfileResult.RefreshToken -> _registrationUiState.update { currentUiState ->
                    currentUiState.copy(successState = RegistrationUiState.SuccessState.RefreshToken)
                }

                UseCaseProfileResult.Success -> _registrationUiState.update { currentUiState ->
                    currentUiState.copy(successState = RegistrationUiState.SuccessState.Success)
                }
            }
        }
    }

    fun resetState(){
        _registrationUiState.value = RegistrationUiState.initialize()
    }
}
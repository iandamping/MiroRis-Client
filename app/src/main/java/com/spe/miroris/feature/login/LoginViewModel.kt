package com.spe.miroris.feature.login

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
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {


    val emailFlow: MutableStateFlow<String> = MutableStateFlow("")
    val passwordFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val _loginUiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.initialize())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun setEmailManually(data: String) {
        emailFlow.value = data
    }

    fun setPasswordManually(data: String) {
        passwordFlow.value = data
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val loginData = userUseCase.userLogin(email, password)) {
                is UseCaseProfileResult.Error -> _loginUiState.update { currentUiState ->
                    currentUiState.copy(errorMessage = loginData.message,successState = LoginUiState.SuccessState.Failed)
                }

                is UseCaseProfileResult.Success -> {
                    _loginUiState.update { currentUiState ->
                        currentUiState.copy(successState = LoginUiState.SuccessState.Success)
                    }
                }

                UseCaseProfileResult.RefreshToken -> _loginUiState.update { currentUiState ->
                    currentUiState.copy(successState = LoginUiState.SuccessState.RefreshToken)
                }
            }
        }
    }

    fun resetState(){
        _loginUiState.value = LoginUiState.initialize()
    }
}
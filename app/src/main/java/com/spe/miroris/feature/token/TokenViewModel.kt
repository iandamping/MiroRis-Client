package com.spe.miroris.feature.token

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val phoneModel: String
        get() = Build.MODEL ?: "Model Undefined"

    private val phoneBrand: String
        get() = Build.BRAND ?: "Brand Undefined"


    private val _tokenUiState: MutableStateFlow<TokenUiState> =
        MutableStateFlow(TokenUiState.initialize())
    val tokenUiState: StateFlow<TokenUiState> = _tokenUiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val repositoryData =
                authRepository.getToken(model = phoneModel, brand = phoneBrand, os = "android")) {
                is DomainAuthResult.Error -> _tokenUiState.update { currentUiState ->
                    currentUiState.copy(errorMessage = repositoryData.message)
                }

                DomainAuthResult.Success -> _tokenUiState.update { currentUiState ->
                    currentUiState.copy(isSuccess = true)
                }
            }
        }
    }
}
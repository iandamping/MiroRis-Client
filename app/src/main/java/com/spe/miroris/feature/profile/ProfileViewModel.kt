package com.spe.miroris.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.core.domain.useCase.model.UseCaseProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCase: UserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutUiState: MutableStateFlow<LogoutUiState> =
        MutableStateFlow(LogoutUiState.initialize())
    val logoutUiState: StateFlow<LogoutUiState> = _logoutUiState.asStateFlow()

    private val _profileUiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.initialize())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenLogoutUiState> =
        MutableStateFlow(RefreshTokenLogoutUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenLogoutUiState> =
        _refreshTokenUiState.asStateFlow()


    fun getUserNameFlow(): Flow<String> = useCase.getUserNameFlow()

    fun getUserEmail(): String = useCase.getUserEmail()

    fun getUserEmailFlow(): Flow<String> = useCase.getUserEmailFlow()

    fun getUserImageProfileFlow(): Flow<String> = useCase.getUserImageProfileFlow()

    fun getUserImageProfile(): String = useCase.getUserImageProfile()

    fun getUserProfile(email: String) {
        viewModelScope.launch {
            when (val remoteData = useCase.getProfile(email)) {
                is UseCaseProfileResult.Error -> _profileUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = ProfileUiState.SuccessState.Error
                    )
                }

                UseCaseProfileResult.Success -> _profileUiState.update { currentUiState ->
                    currentUiState.copy(successState = ProfileUiState.SuccessState.Success)
                }

                UseCaseProfileResult.RefreshToken -> _profileUiState.update { currentUiState ->
                    currentUiState.copy(successState = ProfileUiState.SuccessState.RefreshToken)
                }
            }
        }
    }


    fun userLogout(email: String) {
        viewModelScope.launch {
            when (val remoteData = useCase.logoutUser(email)) {
                is UseCaseProfileResult.Error -> _logoutUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = LogoutUiState.SuccessState.Failed
                    )
                }

                UseCaseProfileResult.Success -> _logoutUiState.update { currentUiState ->
                    currentUiState.copy(successState = LogoutUiState.SuccessState.Success)
                }

                UseCaseProfileResult.RefreshToken -> _logoutUiState.update { currentUiState ->
                    currentUiState.copy(successState = LogoutUiState.SuccessState.RefreshToken)
                }
            }
        }
    }

    fun refreshToken(email: String, caller: ProfileCaller) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = RefreshTokenLogoutUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = when (caller) {
                            ProfileCaller.Profile -> RefreshTokenLogoutUiState.SuccessState.SuccessProfile
                            ProfileCaller.Logout -> RefreshTokenLogoutUiState.SuccessState.SuccessLogout
                        }
                    )
                }
            }
        }
    }

    fun resetState() {
        _logoutUiState.value = LogoutUiState.initialize()
        _refreshTokenUiState.value = RefreshTokenLogoutUiState.initialize()
    }
}
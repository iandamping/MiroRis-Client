package com.spe.miroris.feature.login

import com.spe.miroris.core.domain.common.DomainUserResponseResult
import com.spe.miroris.core.domain.common.DomainVoidLoginResult
import com.spe.miroris.core.domain.repository.UserRepository
import com.spe.miroris.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var sut: LoginViewModel
    private val userRepository: UserRepository = mockk()
//
//    @Before
//    fun setUp() {
//        sut = LoginViewModel(userRepository)
//    }
//
//
//    @Test
//    fun `When sut login should return Success`() = runTest {
//        coEvery { userRepository.getFirebaseToken() } returns DomainUserResponseResult.Success("token")
//        coEvery { userRepository.userLogin(any(), any(), any()) } returns DomainVoidLoginResult.Success
//
//
//        Assert.assertEquals("", sut.loginUiState.value.errorMessage) // Assert on the initial value
//        Assert.assertFalse(sut.loginUiState.value.isSuccess) // Assert on the initial value
//
//        sut.login("a","a")
//
//        Assert.assertEquals("", sut.loginUiState.value.errorMessage) // Assert on the latest value
//        Assert.assertTrue(sut.loginUiState.value.isSuccess) // Assert on the latest value
//    }
//
//    @Test
//    fun `When sut login should return Error from getFirebaseToken`() = runTest {
//        coEvery { userRepository.getFirebaseToken() } returns DomainUserResponseResult.Error("error token")
//        coEvery { userRepository.userLogin(any(), any(), any()) } returns DomainVoidLoginResult.Success
//
//
//        Assert.assertEquals("", sut.loginUiState.value.errorMessage) // Assert on the initial value
//        Assert.assertFalse(sut.loginUiState.value.isSuccess) // Assert on the initial value
//
//        sut.login("a","a")
//
//        Assert.assertEquals("error token", sut.loginUiState.value.errorMessage) // Assert on the latest value
//        Assert.assertFalse(sut.loginUiState.value.isSuccess) // Assert on the latest value
//    }
//
//
//    @Test
//    fun `When sut login should return Error from userLogin`() = runTest {
//        coEvery { userRepository.getFirebaseToken() } returns DomainUserResponseResult.Success("token")
//        coEvery { userRepository.userLogin(any(), any(), any()) } returns DomainVoidLoginResult.Error("login error")
//
//
//        Assert.assertEquals("", sut.loginUiState.value.errorMessage) // Assert on the initial value
//        Assert.assertFalse(sut.loginUiState.value.isSuccess) // Assert on the initial value
//
//        sut.login("a","a")
//
//        Assert.assertEquals("login error", sut.loginUiState.value.errorMessage) // Assert on the latest value
//        Assert.assertFalse(sut.loginUiState.value.isSuccess) // Assert on the latest value
//    }
}
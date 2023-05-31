package com.spe.miroris.core.data.repository

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
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProfileResponse
import com.spe.miroris.core.domain.common.DomainUserResponseResult
import com.spe.miroris.core.domain.common.DomainVoidLoginResult
import com.spe.miroris.core.domain.repository.UserRepository
import com.spe.miroris.core.presentation.helper.UtilityHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var sut: UserRepository
    private val remoteDataSource: RemoteDataSource = mockk()
    private val cacheDataSource: CacheDataSource = mockk()
    private val utilityHelper: UtilityHelper = mockk()

    @Before
    fun setUp() {
        sut = UserRepositoryImpl(
            remoteDataSource = remoteDataSource,
            cacheDataSource = cacheDataSource,
            utilityHelperImpl = utilityHelper
        )

        every { utilityHelper.getString(any()) } returns "random string"
        every { cacheDataSource.getUserBearer() } returns "bearer"
        every { cacheDataSource.getUserToken() } returns "access token"
        every { cacheDataSource.getDeviceID() } returns "id"
    }

    @Test
    fun `When repository invoke userLogin return Success`() = runTest {
        //given
        val loginResponse = LoginResponse("a", 1, "a")
        coEvery {
            remoteDataSource.userLogin(
                any(),
                any(),
                any(),
                any()
            )
        } returns LoginRemoteResult.Success(loginResponse)

        justRun { cacheDataSource.saveUserToken(any()) }
        justRun { cacheDataSource.saveUserEmail(any()) }
        //when
        val result = sut.userLogin("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.userLogin(any(), any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserToken(any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserEmail(any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }

    @Test
    fun `When repository invoke userLogin return Error from encryption error`() = runTest {
        //given
        coEvery {
            remoteDataSource.userLogin(
                any(),
                any(),
                any(),
                any()
            )
        } returns LoginRemoteResult.EncryptionError

        justRun { cacheDataSource.saveUserToken(any()) }
        justRun { cacheDataSource.saveUserEmail(any()) }
        //when
        val result = sut.userLogin("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.userLogin(any(), any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("random string"), result
        )
    }

    @Test
    fun `When repository invoke userLogin return Error from remote error`() = runTest {
        //given
        coEvery {
            remoteDataSource.userLogin(
                any(),
                any(),
                any(),
                any()
            )
        } returns LoginRemoteResult.Error("remote error")

        justRun { cacheDataSource.saveUserToken(any()) }
        justRun { cacheDataSource.saveUserEmail(any()) }
        //when
        val result = sut.userLogin("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.userLogin(any(), any(), any(), any()) })

        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke registerUser return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.registerUser(
                any(),
                any(),
                any(),
                any()
            )
        } returns RegisterRemoteResult.Success

        //when
        val result = sut.registerUser("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.registerUser(any(), any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }

    @Test
    fun `When repository invoke registerUser return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.registerUser(
                any(),
                any(),
                any(),
                any()
            )
        } returns RegisterRemoteResult.Error("remote error")

        //when
        val result = sut.registerUser("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.registerUser(any(), any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke getProfile return Success`() = runTest {
        //given
        val profileResponse = ProfileResponse("a", "a", "a", "a")
        coEvery {
            remoteDataSource.getProfile(
                any(),
                any()
            )
        } returns ProfileRemoteResult.Success(profileResponse)

        justRun { cacheDataSource.saveUserAddress(any()) }
        justRun { cacheDataSource.saveUserEmail(any()) }
        justRun { cacheDataSource.saveUserCity(any()) }
        justRun { cacheDataSource.saveUserName(any()) }
        justRun { cacheDataSource.saveUserImageProfile(any()) }

        //when
        val result = sut.getProfile("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getProfile(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserAddress(any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserEmail(any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserCity(any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserName(any()) })
        coVerify(exactly = 1, verifyBlock = { cacheDataSource.saveUserImageProfile(any()) })
        coVerifySequence {
            cacheDataSource.getUserBearer()
            cacheDataSource.getUserToken()
            remoteDataSource.getProfile(any(), any())
            cacheDataSource.saveUserEmail(any())
            cacheDataSource.saveUserAddress(any())
            cacheDataSource.saveUserCity(any())
            cacheDataSource.saveUserName(any())
            cacheDataSource.saveUserImageProfile(any())
        }
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }


    @Test
    fun `When repository invoke getProfile return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.getProfile(
                any(),
                any()
            )
        } returns ProfileRemoteResult.Error("remote error")

        //when
        val result = sut.getProfile("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getProfile(any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke uploadImage return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.uploadUserImage(
                any(),
                any(),
                any()
            )
        } returns UploadResult.Success

        //when
        val result = sut.uploadUserImage(MultipartBody.Part.createFormData("a", "a"), "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.uploadUserImage(any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }

    @Test
    fun `When repository invoke uploadImage return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.uploadUserImage(
                any(),
                any(),
                any()
            )
        } returns UploadResult.Error("remote error")

        //when
        val result = sut.uploadUserImage(MultipartBody.Part.createFormData("a", "a"), "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.uploadUserImage(any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke editUser return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.editUser(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns EditUserResult.Success

        //when
        val result = sut.editUser("a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.editUser(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }


    @Test
    fun `When repository invoke editUser return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.editUser(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns EditUserResult.Error("remote error")

        //when
        val result = sut.editUser("a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.editUser(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke resetPassword return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.resetPassword(
                any(),
                any(),
                any(),
                any(),
            )
        } returns ResetPasswordResult.Success

        //when
        val result = sut.resetPassword("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.resetPassword(any(), any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }


    @Test
    fun `When repository invoke resetPassword return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.resetPassword(
                any(),
                any(),
                any(),
                any(),
            )
        } returns ResetPasswordResult.Error("error remote")

        //when
        val result = sut.resetPassword("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.resetPassword(any(), any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("error remote"), result
        )
    }


    @Test
    fun `When repository invoke logoutUser return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.logoutUser(
                any(),
                any(),
                any(),
            )
        } returns LogoutRemoteResult.Success

        justRun { cacheDataSource.deleteUserRelatedData() }

        //when
        val result = sut.logoutUser("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.logoutUser(any(), any(), any()) })
        verify(exactly = 1, verifyBlock = { cacheDataSource.deleteUserRelatedData() })

        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Success, result
        )
    }

    @Test
    fun `When repository invoke logoutUser return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.logoutUser(
                any(),
                any(),
                any(),
            )
        } returns LogoutRemoteResult.Error("remote error")


        //when
        val result = sut.logoutUser("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.logoutUser(any(), any(), any()) })

        //then
        Assert.assertEquals(
            DomainVoidLoginResult.Error("remote error"), result
        )
    }


    @Test
    fun `When repository invoke getFirebaseToken return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.getFirebaseToken()
        } returns FirebaseIdResult.Success("token")

        //when
        val result = sut.getFirebaseToken()
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getFirebaseToken() })

        //then
        Assert.assertEquals(
            DomainUserResponseResult.Success("token"), result
        )
    }



    @Test
    fun `When repository invoke getFirebaseToken return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.getFirebaseToken()
        } returns FirebaseIdResult.Error("error")

        //when
        val result = sut.getFirebaseToken()
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getFirebaseToken() })


        //then
        Assert.assertEquals(
            DomainUserResponseResult.Error("random string"), result
        )
    }

}
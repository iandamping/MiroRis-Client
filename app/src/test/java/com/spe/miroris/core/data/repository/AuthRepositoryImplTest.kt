package com.spe.miroris.core.data.repository

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.RefreshTokenResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.presentation.helper.UtilityHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var sut: AuthRepository
    private val remoteDataSource: RemoteDataSource = mockk()
    private val cacheDataSource: CacheDataSource = mockk()
    private val utilityHelper: UtilityHelper = mockk()

    @Before
    fun setUp() {
        sut = AuthRepositoryImpl(
            remoteDataSource = remoteDataSource,
            cacheDataSource = cacheDataSource,
            utilityHelperImpl = utilityHelper
        )
        every { cacheDataSource.getUserToken() } returns "token"
        every { cacheDataSource.getUserBearer() } returns "bearer"
        every { utilityHelper.getString(any()) } returns "string"
    }


    @Test
    fun `when invoke getToken return Success`() = runTest {
        //given
        val tokenResponse = TokenResponse("a", "a", 1)
        coEvery {
            remoteDataSource.getToken(
                any(),
                any(),
                any(),
                any()
            )
        } returns TokenRemoteResult.Success(tokenResponse)
        justRun { cacheDataSource.saveUserToken(any()) }
        justRun { cacheDataSource.saveUserBearer(any()) }
        //when
        val result = sut.getToken("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getToken(any(), any(), any(), any()) })
        coVerify(
            exactly = 1,
            verifyBlock = { cacheDataSource.saveUserToken(any()) })
        coVerify(
            exactly = 1,
            verifyBlock = { cacheDataSource.saveUserBearer(any()) })

        Assert.assertEquals(
            DomainAuthResult.Success, result
        )
    }

    @Test
    fun `when invoke getToken return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.getToken(
                any(),
                any(),
                any(),
                any()
            )
        } returns TokenRemoteResult.Error("Failed")
        //when
        val result = sut.getToken("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getToken(any(), any(), any(), any()) })

        Assert.assertEquals(
            DomainAuthResult.Error("Failed"), result
        )
    }

    @Test
    fun `when invoke getToken return Error encryption from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.getToken(
                any(),
                any(),
                any(),
                any()
            )
        } returns TokenRemoteResult.EncryptionError
        //when
        val result = sut.getToken("a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getToken(any(), any(), any(), any()) })

        Assert.assertEquals(
            DomainAuthResult.Error("string"), result
        )
    }


    @Test
    fun `when invoke refreshToken return Success`() = runTest {
        //given
        val tokenResponse = RefreshTokenResponse("a", 1)
        coEvery {
            remoteDataSource.refreshToken(
                any(),
                any()
            )
        } returns RefreshTokenRemoteResult.Success(tokenResponse)

        justRun { cacheDataSource.saveUserToken(any()) }
        //when
        val result = sut.refreshToken("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.refreshToken(any(), any()) })
        coVerify(
            exactly = 1,
            verifyBlock = { cacheDataSource.saveUserToken(any()) })

        Assert.assertEquals(
            DomainAuthResult.Success, result
        )
    }

    @Test
    fun `when invoke refreshToken return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.refreshToken(
                any(),
                any()
            )
        } returns RefreshTokenRemoteResult.Error("Failed")
        //when
        val result = sut.refreshToken("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.refreshToken(any(), any()) })

        Assert.assertEquals(
            DomainAuthResult.Error("Failed"), result
        )
    }

    @Test
    fun `when invoke refreshToken return Error encryption from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.refreshToken(
                any(),
                any()
            )
        } returns RefreshTokenRemoteResult.EncryptionError

        //when
        val result = sut.refreshToken("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.refreshToken(any(), any()) })


        Assert.assertEquals(
            DomainAuthResult.Error("string"), result
        )
    }
}
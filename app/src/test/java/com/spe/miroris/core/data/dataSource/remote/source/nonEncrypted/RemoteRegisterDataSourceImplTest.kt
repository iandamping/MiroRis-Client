package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteVoidResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RemoteRegisterDataSourceImplTest {

    private lateinit var sut: RemoteRegisterDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    @Before
    fun setUp() {
        sut = RemoteRegisterDataSourceImpl(
            apiInterface,
            remoteHelper,
            utilityHelper,
            encryptionManager
        )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }

    @Test
    fun `when datasource call registerUser should return Success`() = runTest {
        //given
        val successResponse = RegisterRemoteResult.Success
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Success
        //when
        val actualResult = sut.registerUser("a","a","a","a")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.nonEncryptionVoidCall(apiInterface.registerUser(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }
    @Test
    fun `when datasource call registerUser should return Error`() = runTest {
        //given
        val successResponse = RegisterRemoteResult.SourceError("Failed")
        coEvery {
            remoteHelper.nonEncryptionVoidCall(
                apiInterface.registerUser(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteVoidResult.Error(Exception("Failed"))
        //when
        val actualResult = sut.registerUser("a","a","a","a")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.nonEncryptionVoidCall(apiInterface.registerUser(any(),any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.registerUser(any(),any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 3, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

}
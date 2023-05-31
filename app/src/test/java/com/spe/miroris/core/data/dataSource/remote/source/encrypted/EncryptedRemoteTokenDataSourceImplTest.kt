package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteEncryptedResult
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.security.keyProvider.KeyProvider
import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class EncryptedRemoteTokenDataSourceImplTest {

    private lateinit var sut: EncryptedRemoteTokenDataSource
    private val apiInterface: EncryptedApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()
    private val keyProvider: KeyProvider = mockk()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

    @Before
    fun setUp() {
        sut = EncryptedRemoteTokenDataSourceImpl(
            api = apiInterface,
            remoteHelper = remoteHelper,
            utilityHelper = utilityHelper,
            moshiParser = moshiParser,
            encryptionManager = encryptionManager,
            keyProvider = keyProvider
        )
        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
        every { keyProvider.provideAuthVersion() } returns "auth"
        every { keyProvider.provideClientId() } returns "id"
        every { keyProvider.provideClientSecret() } returns "secret"
        every { encryptionManager.decryptRsa(any()) } returns "decrypted rsa"
    }


    @Test
    fun `when datasource call getToken should return Success`() = runTest {
        //given
        val tokenResponse = TokenResponse("decrypted rsa", "Bearer", 3600)
        val successResponse = TokenRemoteResult.Success(tokenResponse)
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":200,\"message_id\":\"Sukses\",\"message_en\":\"Success\",\"data\":{\"access_token\":\"Xio4wmTArVV\\/0R86iJ6+Gc8GjCOqCDc1fFz\\/eGkF5AQbIXl2cld+1y75vjp4ILdqWsD2Unexo7KakrSjelJkGzywxdRwt12ASKSY\\/QFg0raLjloJqDGeOqaMFbKVVJJqFoNZvlwsQrwC32N0GRjiXP7b9BazBrrKlkK8c58h3VnyoNMP1BAl85Fck2XqCy1pz0hQIrPVy\\/1Sx\\/PrbdKG7KIJslmKw3Ay4p+u6xg4hk1vVJnxdBByzwuw3thmuJTSUlrss6hPcoBcgjZYSD5vq05nYIY0uzAV6LzkWJFQaZdHSf5qABbcZ94sBDx\\/+5Xp1Re1srfRDpSY4MDjIkBfZkuLsVIMKcWft3j0QnGozp2ZTufG2bZNOiLJ+D986qsbSoU7hEb+OdOKIi0nAcFJ4SLfZ68ZbWdDrhsWJ\\/TKZW8\\/DdGUKsxXk2JEbB1DWKYHPbwLLO9WRX9hsAE+ePAYTa98dFNPrKf+PfVDuvEvxUHaUV+XG7yf3GWFizag\\/5c\\/\",\"type\":\"Bearer\",\"expired\":\"3600\"}}"


        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.getToken(
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success("random string")
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.decryptRsa(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from code 201`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("Failed")
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":201,\"message_id\":\"Gagal\",\"message_en\":\"Failed\",\"data\":{\"access_token\":\"Xio4wmTArVV\\/0R86iJ6+Gc8GjCOqCDc1fFz\\/eGkF5AQbIXl2cld+1y75vjp4ILdqWsD2Unexo7KakrSjelJkGzywxdRwt12ASKSY\\/QFg0raLjloJqDGeOqaMFbKVVJJqFoNZvlwsQrwC32N0GRjiXP7b9BazBrrKlkK8c58h3VnyoNMP1BAl85Fck2XqCy1pz0hQIrPVy\\/1Sx\\/PrbdKG7KIJslmKw3Ay4p+u6xg4hk1vVJnxdBByzwuw3thmuJTSUlrss6hPcoBcgjZYSD5vq05nYIY0uzAV6LzkWJFQaZdHSf5qABbcZ94sBDx\\/+5Xp1Re1srfRDpSY4MDjIkBfZkuLsVIMKcWft3j0QnGozp2ZTufG2bZNOiLJ+D986qsbSoU7hEb+OdOKIi0nAcFJ4SLfZ68ZbWdDrhsWJ\\/TKZW8\\/DdGUKsxXk2JEbB1DWKYHPbwLLO9WRX9hsAE+ePAYTa98dFNPrKf+PfVDuvEvxUHaUV+XG7yf3GWFizag\\/5c\\/\",\"type\":\"Bearer\",\"expired\":\"3600\"}}"


        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.getToken(
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success("random string")
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(), any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from null body`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("response body is null")
        coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any())) } returns RemoteEncryptedResult.Success(
            Response.success(null)
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(),any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getToken should return Error from null data`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("data from service is null")
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":200,\"message_id\":\"Sukses\",\"message_en\":\"Success\",\"data\":null}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.getToken(
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Success(
            Response.success(
                "random string"
            )
        )

        //when
        val actualResult = sut.getToken("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(), any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(),any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getToken should return Error from remoteHelper`() = runTest {
        //given
        val errorResponse = TokenRemoteResult.Error("code : 300 error : Incorrect email or password.")
        every { encryptionManager.decryptAes(any()) } returns "{\"code\":300,\"message_id\":\"gagal\",\"message_en\":\"failed\",\"data\":[{\"field\":\"password\",\"message\":\"Incorrect email or password.\"}]}"
        coEvery {
            remoteHelper.encryptionCall(
                apiInterface.getToken(
                    any(),
                    any()
                )
            )
        } returns RemoteEncryptedResult.Error(
            Exception("Failed")
        )
        //when
        val actualResult = sut.getToken("c", "c", "c", "c")
        coVerify(
            exactly = 1,
            verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(), any())) })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getToken should return Error from EncryptionError`() =
        runTest {
            //given
            coEvery { remoteHelper.encryptionCall(apiInterface.getToken(any(),any())) } returns RemoteEncryptedResult.EncryptionError
            //when
            val actualResult = sut.getToken("c", "c", "c", "c")
            coVerify(
                exactly = 1,
                verifyBlock = { remoteHelper.encryptionCall(apiInterface.getToken(any(), any())) })
            coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                TokenRemoteResult.EncryptionError, actualResult
            )
        }

    @Test
    fun `when datasource call getToken should return Error from catching exception`() =
        runTest {
            //given
            coEvery { apiInterface.getToken(any(), any()) } throws SocketTimeoutException()
            //when
            val actualResult = sut.getToken("c", "c", "c", "c")

            coVerify(exactly = 1, verifyBlock = { apiInterface.getToken(any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 4, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            Assert.assertEquals(
                "Error", (actualResult as TokenRemoteResult.Error).errorMessage
            )
        }
}
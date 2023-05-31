package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelperImpl
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.BanksResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BankResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DecryptedErrorResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.DefaultDecryptedErrorBaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListBankResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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

class RemoteBanksDataSourceImplTest {

    private lateinit var sut: RemoteBanksDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

    @Before
    fun setUp() {
        sut =
            RemoteBanksDataSourceImpl(apiInterface, remoteHelper, utilityHelper, encryptionManager)

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
    }

    @Test
    fun `when datasource call getBank should return Success`() = runTest {
        //given
        val strings = "{\"code\":200,\"message_id\":\"Sukses\",\"message_en\":\"Success\",\"data\":{\"list\":[{\"id\":\"1\",\"nama_bank\":\"Bank BCA\",\"code_bank\":\"014\"},{\"id\":\"2\",\"nama_bank\":\"Bank Mandiri\",\"code_bank\":\"008\"},{\"id\":\"3\",\"nama_bank\":\"Bank BNI\",\"code_bank\":\"009\"},{\"id\":\"4\",\"nama_bank\":\"Bank Syariah Indonesia (eks BNI Syariah)*\",\"code_bank\":\"427\"},{\"id\":\"5\",\"nama_bank\":\"Bank BRI\",\"code_bank\":\"002\"},{\"id\":\"6\",\"nama_bank\":\"Bank Syariah Indonesia (eks Mandiri Syariah)*\",\"code_bank\":\"451\"},{\"id\":\"7\",\"nama_bank\":\"Bank CIMB Niaga\",\"code_bank\":\"022\"},{\"id\":\"8\",\"nama_bank\":\"Bank CIMB Niaga Syariah\",\"code_bank\":\"022\"},{\"id\":\"9\",\"nama_bank\":\"Bank Muamalat\",\"code_bank\":\"147\"},{\"id\":\"10\",\"nama_bank\":\"Bank Tabungan Pensiunan Nasional (BTPN)\",\"code_bank\":\"213\"},{\"id\":\"11\",\"nama_bank\":\"JENIUS\",\"code_bank\":\"213\"},{\"id\":\"12\",\"nama_bank\":\"Bank Syariah Indonesia (eks BRI Syariah)*\",\"code_bank\":\"422\"},{\"id\":\"13\",\"nama_bank\":\"Bank Tabungan Negara (BTN)\",\"code_bank\":\"200\"},{\"id\":\"14\",\"nama_bank\":\"Permata Bank\",\"code_bank\":\"013\"},{\"id\":\"15\",\"nama_bank\":\"Bank Danamon\",\"code_bank\":\"011\"},{\"id\":\"16\",\"nama_bank\":\"Bank BII Maybank\",\"code_bank\":\"016\"},{\"id\":\"17\",\"nama_bank\":\"Bank Mega\",\"code_bank\":\"426\"},{\"id\":\"18\",\"nama_bank\":\"Bank Sinarmas\",\"code_bank\":\"153\"},{\"id\":\"19\",\"nama_bank\":\"Bank Commonwealth\",\"code_bank\":\"950\"},{\"id\":\"20\",\"nama_bank\":\"Bank OCBC NISP\",\"code_bank\":\"028\"}],\"countdata\":60}}"
        val types =
            Types.newParameterizedType(
                BaseResponse::class.java,
                ListBankResponse::class.java
            )

        val data = moshiParser.fromJson<BaseResponse<ListBankResponse>>(
            strings,
            types
        )

        val bankResponse =
            ListBankResponse(listOfBank = listOf(BankResponse("1", "a", "a")), 1,)
        val successResponse = BanksResult.Success(bankResponse)
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getBanks(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success( data
            )
        )
        //when
        val actualResult = sut.getBank("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getBanks(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getBanks(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getBank should return Error from code 201`() = runTest {
        //given
        val bankResponse =
            ListBankResponse(listOfBank = listOf(BankResponse("1", "a", "a")), 1)
        val successResponse = BanksResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getBanks(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 201,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagagal",
                    data = bankResponse
                )
            )
        )
        //when
        val actualResult = sut.getBank("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getBanks(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getBanks(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getBank should return Error from null body`() = runTest {
        //given
        val successResponse = BanksResult.Error("data from service is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getBanks(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 200,
                    messageEnglish = "Success",
                    messageIndonesia = "Sukses",
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getBank("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getBanks(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getBanks(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getBank should return Error from null data`() = runTest {
        //given
        val successResponse = BanksResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.getBanks(
                    any(),
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.getBank("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.getBanks(
                        any(),
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.getBanks(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getBank should return Error from catching Exceptions`() = runTest {
        //given
        coEvery {
            apiInterface.getBanks(
                any(),
                any(),
                any()
            )
        } throws SocketTimeoutException()
        //when
        val actualResult = sut.getBank("a", "a", "a", "a")
        coVerify(exactly = 1, verifyBlock = { apiInterface.getBanks(any(), any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        //then
        Assert.assertEquals(
            "Error", (actualResult as BanksResult.Error).errorMessage
        )
    }

}
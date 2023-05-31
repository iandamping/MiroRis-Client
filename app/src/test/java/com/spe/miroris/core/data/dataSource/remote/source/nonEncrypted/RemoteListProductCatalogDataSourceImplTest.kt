package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class RemoteListProductCatalogDataSourceImplTest {

    private lateinit var sut: RemoteProductCatalogDataSource
    private val apiInterface: ApiInterface = mockk()
    private val remoteHelper: RemoteHelper = mockk()
    private val utilityHelper: UtilityHelper = mockk()
    private val encryptionManager: EncryptionManager = mockk()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var moshiParser: MoshiParser = MoshiParserImpl(moshi)

//    @Before
//    fun setUp() {
//        sut =
//            RemoteProductCatalogDataSourceImpl(
//                apiInterface,
//                remoteHelper,
//                utilityHelper,
//                encryptionManager,
//                moshiParser
//            )
//
//        every { utilityHelper.getString(any()) } returns "Error"
//        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
//        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
//    }
//
//    @Test
//    fun `when datasource call getProductCatalog should return Success`() = runTest {
//        //given
//        val productResponse =
//            ListProductCatalogResponse(
//                listOf(
//                    ProductCatalogResponse(
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                    )
//                ),
//                1, "a", "a",
//            )
//        val successResponse = ProductCatalogResult.Success(productResponse)
//        coEvery {
//            remoteHelper.nonEncryptionCall(
//                apiInterface.productCatalog(
//                    any(),
//                    any(),
//                )
//            )
//        } returns RemoteResult.Success(
//            Response.success(
//                BaseResponse(
//                    code = 200,
//                    messageEnglish = "Success",
//                    messageIndonesia = "Sukses",
//                    data = productResponse
//                )
//            )
//        )
//        //when
//        val actualResult = sut.getProductCatalog("a", "a", "a", "a")
//        coVerify(
//            exactly = 1,
//            verifyBlock = {
//                remoteHelper.nonEncryptionCall(
//                    apiInterface.productCatalog(
//                        any(),
//                        any(),
//                    )
//                )
//            })
//        coVerify(exactly = 1, verifyBlock = { apiInterface.productCatalog(any(), any()) })
//        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
//        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
//        //then
//        assertEquals(
//            successResponse, actualResult
//        )
//    }
//
//    @Test
//    fun `when datasource call getProductCatalog should return Error`() = runTest {
//        //given
//        val errorResponse = ProductCatalogResult.Error("Page not found.")
//        coEvery {
//            remoteHelper.nonEncryptionCall(
//                apiInterface.productCatalog(
//                    any(),
//                    any()
//                )
//            )
//        } returns RemoteResult.Error(Exception("{\"code\":0,\"status\":404,\"name\":\"Not Found\",\"message\":\"Page not found.\"}"))
//        //when
//        val actualResult = sut.getProductCatalog("a", "a", "a", "a")
//        coVerify(
//            exactly = 1,
//            verifyBlock = {
//                remoteHelper.nonEncryptionCall(
//                    apiInterface.productCatalog(
//                        any(),
//                        any()
//                    )
//                )
//            })
//        coVerify(exactly = 1, verifyBlock = { apiInterface.productCatalog(any(), any()) })
//        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
//        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
//        //then
//        assertEquals(
//            errorResponse, actualResult
//        )
//    }
//
//    @Test
//    fun `when datasource call getProductCatalog should return Error from code 201`() = runTest {
//        //given
//        val productResponse =
//            ListProductCatalogResponse(
//                listOf(
//                    ProductCatalogResponse(
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                        "a",
//                    )
//                ),
//                1, "a", "a",
//            )
//        val errorResponse = ProductCatalogResult.Error("Failed")
//        coEvery {
//            remoteHelper.nonEncryptionCall(
//                apiInterface.productCatalog(
//                    any(),
//                    any()
//                )
//            )
//        } returns RemoteResult.Success(
//            Response.success(
//                BaseResponse(
//                    code = 201,
//                    messageEnglish = "Failed",
//                    messageIndonesia = "Gagal",
//                    data = productResponse
//                )
//            )
//        )
//        //when
//        val actualResult = sut.getProductCatalog("a", "a", "a", "a")
//        coVerify(
//            exactly = 1,
//            verifyBlock = {
//                remoteHelper.nonEncryptionCall(
//                    apiInterface.productCatalog(
//                        any(),
//                        any(),
//                    )
//                )
//            })
//        coVerify(exactly = 1, verifyBlock = { apiInterface.productCatalog(any(), any()) })
//        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
//        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
//        //then
//        assertEquals(
//            errorResponse, actualResult
//        )
//    }

    @Test
    fun `when datasource call getProductCatalog should return Error from null data`() = runTest {
        //given
        val errorResponse = ProductCatalogResult.Error("data from service is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.productCatalog(
                    any(),
                    any(),
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 200,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagal",
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getProductCatalog("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.productCatalog(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.productCatalog(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductCatalog should return Error from null body`() = runTest {
        //given
        val errorResponse = ProductCatalogResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.productCatalog(
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                null
            )
        )
        //when
        val actualResult = sut.getProductCatalog("a", "a", "a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.productCatalog(
                        any(),
                        any()
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.productCatalog(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getProductCatalog should return Error from catching exception`() =
        runTest {
            //given
            coEvery {
                apiInterface.productCatalog(
                    any(),
                    any()
                )
            } throws SocketTimeoutException()
            //when
            val actualResult = sut.getProductCatalog("a", "a", "a", "a")

            coVerify(
                exactly = 1,
                verifyBlock = { apiInterface.productCatalog(any(), any()) })
            coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
            coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
            //then
            assertEquals(
                "Error", (actualResult as ProductCatalogResult.Error).errorMessage
            )
        }
}
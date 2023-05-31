package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.helper.RemoteHelper
import com.spe.miroris.core.data.dataSource.remote.helper.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.CategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteCategoryProductDataSourceImplTest {
    private lateinit var sut: RemoteCategoryProductDataSource
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
            RemoteCategoryProductDataSourceImpl(
                apiInterface,
                remoteHelper,
                utilityHelper,
                encryptionManager,
                moshiParser
            )

        every { utilityHelper.getString(any()) } returns "Error"
        every { encryptionManager.createHmacSignature(any()) } returns "encrypted hmac"
        every { encryptionManager.encryptRsa(any()) } returns "encrypted rsa"
    }

    @Test
    fun `when datasource call getCategoryProduct should return Success`() = runTest {
        //given
        val productResponse =
            ListCategoryProductResponse(
                listOf(
                    CategoryProductResponse(
                        "a",
                        "a",
                        "a",
                    )
                ),
                "a", 1, 1,
            )
        val successResponse = CategoryProductResult.Success(productResponse)
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.categoryProduct(
                    any(),
                    any(),
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 200,
                    messageEnglish = "Success",
                    messageIndonesia = "Sukses",
                    data = productResponse
                )
            )
        )
        //when
        val actualResult = sut.getCategoryProduct("a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.categoryProduct(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.categoryProduct(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            successResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getCategoryProduct should return Error from 201`() = runTest {
        //given
        val productResponse =
            ListCategoryProductResponse(
                listOf(
                    CategoryProductResponse(
                        "a",
                        "a",
                        "a",
                    )
                ),
                "a", 1, 1,
            )
        val errorResponse = CategoryProductResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.categoryProduct(
                    any(),
                    any(),
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 201,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagal",
                    data = productResponse
                )
            )
        )
        //when
        val actualResult = sut.getCategoryProduct("a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.categoryProduct(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.categoryProduct(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getCategoryProduct should return Error from null data`() = runTest {
        //given
        val errorResponse = CategoryProductResult.Error("Failed")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.categoryProduct(
                    any(),
                    any(),
                )
            )
        } returns RemoteResult.Success(
            Response.success(
                BaseResponse(
                    code = 201,
                    messageEnglish = "Failed",
                    messageIndonesia = "Gagal",
                    data = null
                )
            )
        )
        //when
        val actualResult = sut.getCategoryProduct("a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.categoryProduct(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.categoryProduct(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }

    @Test
    fun `when datasource call getCategoryProduct should return Error from null body`() = runTest {
        //given
        val errorResponse = CategoryProductResult.Error("response body is null")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.categoryProduct(
                    any(),
                    any(),
                )
            )
        } returns RemoteResult.Success(
            Response.success(null)
        )
        //when
        val actualResult = sut.getCategoryProduct("a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.categoryProduct(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.categoryProduct(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }


    @Test
    fun `when datasource call getCategoryProduct should return Error`() = runTest {
        //given
        val errorResponse = CategoryProductResult.Error("Page not found.")
        coEvery {
            remoteHelper.nonEncryptionCall(
                apiInterface.categoryProduct(
                    any(),
                    any()
                )
            )
        } returns RemoteResult.Error(Exception("{\"code\":0,\"status\":404,\"name\":\"Not Found\",\"message\":\"Page not found.\"}"))
        //when
        val actualResult = sut.getCategoryProduct("a", "a")
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteHelper.nonEncryptionCall(
                    apiInterface.categoryProduct(
                        any(),
                        any(),
                    )
                )
            })
        coVerify(exactly = 1, verifyBlock = { apiInterface.categoryProduct(any(), any()) })
        coVerify(exactly = 1, verifyBlock = { encryptionManager.createHmacSignature(any()) })
        coVerify(exactly = 2, verifyBlock = { encryptionManager.encryptRsa(any()) })
        //then
        Assert.assertEquals(
            errorResponse, actualResult
        )
    }
}
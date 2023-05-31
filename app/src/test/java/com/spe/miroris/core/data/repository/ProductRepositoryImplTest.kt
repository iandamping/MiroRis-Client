package com.spe.miroris.core.data.repository

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.CategoryProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.DeactivateProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.EditProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCatalogResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductCreateResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ProductListUserResult
import com.spe.miroris.core.data.dataSource.remote.model.common.UploadProductResult
import com.spe.miroris.core.data.dataSource.remote.model.common.ViewProductImageResult
import com.spe.miroris.core.data.dataSource.remote.model.response.CategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListCategoryProductResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ListProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductCatalogResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductListUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.ViewProductResponse
import com.spe.miroris.core.domain.model.CategoryProduct
import com.spe.miroris.core.domain.model.ListCategoryProduct
import com.spe.miroris.core.domain.model.ListProductCatalog
import com.spe.miroris.core.domain.model.ProductCatalog
import com.spe.miroris.core.domain.common.DomainProductResult
import com.spe.miroris.core.domain.common.DomainVoidProductResult
import com.spe.miroris.core.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var sut: ProductRepository
    private val remoteDataSource: RemoteDataSource = mockk()
    private val cacheDataSource: CacheDataSource = mockk()

    @Before
    fun setUp() {
        sut = ProductRepositoryImpl(remoteDataSource, cacheDataSource)
        every { cacheDataSource.getUserBearer() } returns "bearer"
        every { cacheDataSource.getUserToken() } returns "token"
    }

    @Test
    fun `When call getProductListUser return Success`() = runTest {
        //given
        val productResponse = ProductListUserResponse(
            listOf(
                ProductUserResponse(
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a",
                    "a"
                )
            ), 1
        )
        coEvery {
            remoteDataSource.getProductListUser(
                any(),
                any(),
                any(),
            )
        } returns ProductListUserResult.Success(productResponse)
        //when
        val result = sut.getProductListUser("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getProductListUser(any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainProductResult.Success(productResponse), result
        )
    }

    @Test
    fun `When call getProductListUser return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.getProductListUser(
                any(),
                any(),
                any(),
            )
        } returns ProductListUserResult.Error("failed")
        //when
        val result = sut.getProductListUser("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = { remoteDataSource.getProductListUser(any(), any(), any()) })
        //then
        Assert.assertEquals(
            DomainProductResult.Error("failed"), result
        )
    }
//
//
//    @Test
//    fun `When call getProductCatalog return Success`() = runTest {
//        //given
//        val response = ListProductCatalogResponse(
//            listOf(
//                ProductCatalogResponse(
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a",
//                    "a"
//                )
//            ), 1, "a", "a"
//        )
//        val mappingResponse = ListProductCatalog(response.listOfProductCatalog.map { mapData ->
//            ProductCatalog(
//                productId = mapData.productId,
//                categoryId = mapData.categoryId,
//                userId = mapData.userId,
//                productName = mapData.productName,
//                productDetail = mapData.productDetail,
//                productDuration = mapData.productDuration,
//                productTypePayment = mapData.productTypePayment,
//                productType = mapData.productType,
//                productStartFunding = mapData.productStartFunding,
//                productFinishFunding = mapData.productFinishFunding,
//                productQris = mapData.productQris,
//                productPaid = mapData.productPaid,
//                productStatus = mapData.productStatus,
//                createdAt = mapData.createdAt,
//                updatedAt = mapData.updatedAt,
//                categoryName = mapData.categoryName,
//                productDurationName = mapData.productDurationName,
//                productTypePaymentName = mapData.productTypePaymentName,
//                productTypeName = mapData.productTypeName
//            )
//        })
//        coEvery {
//            remoteDataSource.getProductCatalog(
//                any(),
//                any(),
//                any(),
//                any(),
//            )
//        } returns ProductCatalogResult.Success(response)
//        //when
//        val result = sut.getProductCatalog("a", "a", "a", "a")
//        //then
//        coVerify(
//            exactly = 1,
//            verifyBlock = {
//                remoteDataSource.getProductCatalog(
//                    any(),
//                    any(),
//                    any(),
//                    any()
//                )
//            })
//        //then
//        Assert.assertEquals(
//            DomainProductResult.Success(mappingResponse), result
//        )
//    }


    @Test
    fun `When call getProductCatalog return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.getProductCatalog(
                any(),
                any(),
                any(),
                any(),
            )
        } returns ProductCatalogResult.Error("failed")
        //when
        val result = sut.getProductCatalog("a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.getProductCatalog(
                    any(),
                    any(),
                    any(),
                    any()
                )
            })
        //then
        Assert.assertEquals(
            DomainProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call productCreate return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.productCreate(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns ProductCreateResult.Success
        //when
        val result = sut.productCreate("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.productCreate(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
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
            DomainVoidProductResult.Success, result
        )
    }

    @Test
    fun `When call productCreate return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.productCreate(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns ProductCreateResult.Error("failed")
        //when
        val result = sut.productCreate("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.productCreate(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
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
            DomainVoidProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call editProductCreate return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.editProductCreate(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns EditProductCreateResult.Success
        //when
        val result =
            sut.editProductCreate("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.editProductCreate(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
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
            DomainVoidProductResult.Success, result
        )
    }


    @Test
    fun `When call editProductCreate return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.editProductCreate(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns EditProductCreateResult.Error("failed")
        //when
        val result =
            sut.editProductCreate("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.editProductCreate(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
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
            DomainVoidProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call deactivateProduct return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.deactivateProduct(
                any(),
                any(),
                any(),
            )
        } returns DeactivateProductResult.Success
        //when
        val result = sut.deactivateProduct("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.deactivateProduct(
                    any(),
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidProductResult.Success, result
        )
    }


    @Test
    fun `When call deactivateProduct return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.deactivateProduct(
                any(),
                any(),
                any(),
            )
        } returns DeactivateProductResult.Error("failed")
        //when
        val result = sut.deactivateProduct("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.deactivateProduct(
                    any(),
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call uploadProduct return Success`() = runTest {
        //given
        coEvery {
            remoteDataSource.uploadProduct(
                any(),
                any(),
                any(),
            )
        } returns UploadProductResult.Success
        //when
        val result = sut.uploadProduct(MultipartBody.Part.createFormData("a", "a"), "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.uploadProduct(
                    any(),
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidProductResult.Success, result
        )
    }


    @Test
    fun `When call uploadProduct return Error from response`() = runTest {
        //given
        coEvery {
            remoteDataSource.uploadProduct(
                any(),
                any(),
                any(),
            )
        } returns UploadProductResult.Error("failed")
        //when
        val result = sut.uploadProduct(MultipartBody.Part.createFormData("a", "a"), "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.uploadProduct(
                    any(),
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainVoidProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call viewProductImage return Success`() = runTest {
        //given
        val response = ViewProductResponse("a", "a", "a", "a", "a", "a")
        coEvery {
            remoteDataSource.viewProductImage(
                any(),
                any(),
            )
        } returns ViewProductImageResult.Success(listOf(response))
        //when
        val result = sut.viewProductImage("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.viewProductImage(
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainProductResult.Success(listOf(response)), result
        )
    }


    @Test
    fun `When call viewProductImage return Error from remote`() = runTest {
        //given
        coEvery {
            remoteDataSource.viewProductImage(
                any(),
                any(),
            )
        } returns ViewProductImageResult.Error("failed")
        //when
        val result = sut.viewProductImage("a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.viewProductImage(
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainProductResult.Error("failed"), result
        )
    }


    @Test
    fun `When call getCategoryProduct return Success`() = runTest {
        //given
        val response =
            ListCategoryProductResponse(listOf(CategoryProductResponse("a", "a", "a")), "a", 1, 2)
        val mappingResponse =
            ListCategoryProduct(listOfCategoryProduct = response.listOfCategoryProduct.map { mapData ->
                CategoryProduct(
                    categoryName = mapData.categoryName,
                    categoryId = mapData.categoryId
                )
            }, listOfFundType = listOf("profit", "non profit"))
        coEvery {
            remoteDataSource.getCategoryProduct(
                any(),
                any(),
            )
        } returns CategoryProductResult.Success(response)
        //when
        val result = sut.getCategoryProduct("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.getCategoryProduct(
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainProductResult.Success(mappingResponse), result
        )
    }

    @Test
    fun `When call getCategoryProduct return Error`() = runTest {
        //given
        coEvery {
            remoteDataSource.getCategoryProduct(
                any(),
                any(),
            )
        } returns CategoryProductResult.Error("error")
        //when
        val result = sut.getCategoryProduct("a", "a")
        //then
        coVerify(
            exactly = 1,
            verifyBlock = {
                remoteDataSource.getCategoryProduct(
                    any(),
                    any(),
                )
            })
        //then
        Assert.assertEquals(
            DomainProductResult.Error("error"), result
        )
    }


}
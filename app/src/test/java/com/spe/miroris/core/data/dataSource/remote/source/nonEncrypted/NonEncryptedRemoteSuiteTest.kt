package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    RemoteRegisterDataSourceImplTest::class,
    RemoteLogoutDataSourceImplTest::class,
    RemoteBanksDataSourceImplTest::class,
    RemoteResetPasswordDataSourceImplTest::class,
    RemoteProfileDataSourceImplTest::class,
    RemoteUploadImageUserDataSourceImplTest::class,
    RemoteEditUserDataSourceImplTest::class,
    RemoteProductListUserDataSourceImplTest::class,
    RemoteListProductCatalogDataSourceImplTest::class,
    RemoteProductCreateDataSourceImplTest::class,
    RemoteEditProductDataSourceImplTest::class,
    RemoteFundsDataSourceImplTest::class,
    RemoteInvestmentsDataSourceImplTest::class,
    RemoteDetailFundDataSourceImplTest::class,
    RemoteUploadProductDataSourceImplTest::class,
    RemoteViewProductImageDataSourceImplTest::class
)
class NonEncryptedRemoteSuiteTest
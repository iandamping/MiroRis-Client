package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    EncryptedRemoteLoginDataSourceImplTest::class,
    EncryptedRemoteTokenDataSourceImplTest::class,
    EncryptedRemoteRefreshTokenDataSourceImplTest::class
)
class EncryptedRemoteSuiteTest
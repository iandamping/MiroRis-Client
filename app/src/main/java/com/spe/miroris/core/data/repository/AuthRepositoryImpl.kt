package com.spe.miroris.core.data.repository

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.presentation.helper.UtilityHelper
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cacheDataSource: CacheDataSource,
    private val utilityHelperImpl: UtilityHelper
) : AuthRepository {
    override suspend fun getToken(
        model: String,
        brand: String,
        os: String,
    ): DomainAuthResult {
        return when (val remoteData = remoteDataSource.getToken(
            uuid = cacheDataSource.getDeviceID(),
            model = model,
            brand = brand,
            os = os,
        )) {
            TokenRemoteResult.EncryptionError -> DomainAuthResult.Error(
                utilityHelperImpl.getString(
                    R.string.encryption_error_message
                )
            )

            is TokenRemoteResult.Error -> DomainAuthResult.Error(remoteData.errorMessage)
            is TokenRemoteResult.Success -> {
                with(cacheDataSource) {
                    saveAuthToken(remoteData.data.accessToken)
                    saveUserBearer(remoteData.data.type)
                }
                DomainAuthResult.Success
            }
        }
    }

    override suspend fun refreshToken(email: String): DomainAuthResult {
        return when (val remoteData = remoteDataSource.refreshToken(
            email = email,
            token = "${cacheDataSource.getUserBearer()} ${cacheDataSource.getAuthToken()}"
        )) {
            RefreshTokenRemoteResult.EncryptionError -> DomainAuthResult.Error(
                utilityHelperImpl.getString(
                    R.string.encryption_error_message
                )
            )

            is RefreshTokenRemoteResult.Error -> DomainAuthResult.Error(remoteData.errorMessage)
            is RefreshTokenRemoteResult.Success -> {
                cacheDataSource.saveUserToken(remoteData.data.token)
                DomainAuthResult.Success
            }
        }
    }

    override fun getUserToken(): String {
        return cacheDataSource.getUserToken()
    }
}
package com.spe.miroris.core.data.dataSource.remote.helper

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.model.common.EncryptedRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.presentation.helper.UtilityHelper
import retrofit2.Response
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteHelperImpl @Inject constructor(private val utilityHelper: UtilityHelper) :
    RemoteHelper {
    override suspend fun <T> nonEncryptionCall(call: Response<BaseResponse<T>>): RemoteResult<T> {
        return try {
            if (call.isSuccessful) {
                return RemoteResult.Success(call)
            } else return RemoteResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: Exception) {
            RemoteResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: SocketException) {
            RemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: UnknownHostException) {
            RemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: SocketTimeoutException) {
            RemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))
        }
    }


    override suspend fun encryptionCall(call: Response<String>): EncryptedRemoteResult {
        return try {
            if (call.isSuccessful) {
                return EncryptedRemoteResult.Success(call)
            } else return EncryptedRemoteResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: Exception) {
            EncryptedRemoteResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: SocketException) {
            EncryptedRemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: EOFException) {
            EncryptedRemoteResult.EncryptionError

        } catch (e: UnknownHostException) {
            EncryptedRemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: SocketTimeoutException) {
            EncryptedRemoteResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))
        }
    }
}
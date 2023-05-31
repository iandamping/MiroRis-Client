package com.spe.miroris.core.data.dataSource.remote.helper

import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import com.spe.miroris.core.presentation.helper.UtilityHelper
import retrofit2.Response
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteHelperImpl @Inject constructor(private val utilityHelper: UtilityHelper) :
    RemoteHelper {

    companion object {
        const val JSON_NULL = "json is null"
        const val BODY_NULL = "response body is null"
        const val ERROR_MESSAGE_NULL = "error message is null"
        const val DATA_NULL = "data from service is null"
    }
        override suspend fun <T> nonEncryptionCall(call: Response<BaseResponse<T>>): RemoteResult<T> {
        return try {
            if (call.isSuccessful) {
                return RemoteResult.Success(call)
            } else return RemoteResult.Error(Exception(call.errorBody()?.string() ?:utilityHelper.getString(R.string.default_error_message)))
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

    override suspend fun nonEncryptionVoidCall(call: Response<VoidBaseResponse>): RemoteVoidResult {
        return try {
            if (call.isSuccessful) {
                return RemoteVoidResult.Success(call)
            } else return RemoteVoidResult.Error(Exception(call.errorBody()?.string() ?:utilityHelper.getString(R.string.default_error_message)))
        } catch (e: Exception) {
            RemoteVoidResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: SocketException) {
            RemoteVoidResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: UnknownHostException) {
            RemoteVoidResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: SocketTimeoutException) {
            RemoteVoidResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))
        }
    }


    override suspend fun encryptionCall(call: Response<String>): RemoteEncryptedResult {
        return try {
            if (call.isSuccessful) {
                return RemoteEncryptedResult.Success(call)
            } else return RemoteEncryptedResult.Error(Exception(call.errorBody()?.string() ?: utilityHelper.getString(R.string.default_error_message)))
        } catch (e: Exception) {
            RemoteEncryptedResult.Error(Exception(utilityHelper.getString(R.string.default_error_message)))
        } catch (e: SocketException) {
            RemoteEncryptedResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: EOFException) {
            RemoteEncryptedResult.EncryptionError

        } catch (e: UnknownHostException) {
            RemoteEncryptedResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))

        } catch (e: SocketTimeoutException) {
            RemoteEncryptedResult.Error(Exception(utilityHelper.getString(R.string.check_internet_condition)))
        }
    }
}
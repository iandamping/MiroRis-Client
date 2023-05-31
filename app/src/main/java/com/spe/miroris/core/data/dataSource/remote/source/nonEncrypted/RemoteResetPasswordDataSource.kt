package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.ResetPasswordResult


interface RemoteResetPasswordDataSource {

    suspend fun resetPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        token: String
    ) : ResetPasswordResult

}
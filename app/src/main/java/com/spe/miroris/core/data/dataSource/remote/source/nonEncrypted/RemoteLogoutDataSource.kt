package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.LogoutRemoteResult


interface RemoteLogoutDataSource {

    suspend fun logoutUser(email:String,uuid:String,token:String):LogoutRemoteResult

}
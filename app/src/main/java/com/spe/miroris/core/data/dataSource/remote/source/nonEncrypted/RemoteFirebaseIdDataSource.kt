package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.FirebaseIdResult


interface RemoteFirebaseIdDataSource {

    suspend fun getFirebaseToken(): FirebaseIdResult<String>

}
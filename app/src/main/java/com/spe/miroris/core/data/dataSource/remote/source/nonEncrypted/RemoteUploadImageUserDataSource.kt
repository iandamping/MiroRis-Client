package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.UploadResult
import java.io.File


interface RemoteUploadImageUserDataSource {

    suspend fun uploadUserImage(file: File?, email: String, token: String): UploadResult
}
package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.security.EncryptionManager
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by Ian Damping on 02,July,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
class DecryptInterceptor @Inject constructor(
    private val encryptionManager: EncryptionManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain
        .run { proceed(request()) }
        .let { response ->
            return@let if (response.isSuccessful) {
                val body = response.body!!

                val contentType = body.contentType()
                val charset = contentType?.charset() ?: Charset.defaultCharset()
                val buffer = body.source().apply { request(Long.MAX_VALUE) }.buffer
                val bodyContent = buffer.clone().readString(charset)

                response.newBuilder()
                    .body(ResponseBody.create(contentType, bodyContent.let(::decryptBody)))
                    .build()
            } else response
        }

    private fun decryptBody(content: String): String {
        // decryption
        return encryptionManager.decryptAes(
            input = content,
            aesKey = encryptionManager.provideAesKey(),
            ivKey = encryptionManager.provideAesIVKey()
        )
    }
}

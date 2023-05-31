package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.model.common.FirebaseIdResult
import com.spe.miroris.core.presentation.helper.UtilityHelper
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class RemoteFirebaseIdDataSourceImpl @Inject constructor(private val utilityHelper: UtilityHelper) :
    RemoteFirebaseIdDataSource {
    override suspend fun getFirebaseToken(): FirebaseIdResult<String> {
        val firebaseToken: CompletableDeferred<FirebaseIdResult<String>> = CompletableDeferred()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                firebaseToken.complete(
                    FirebaseIdResult.Error(
                        task.exception?.localizedMessage
                            ?: utilityHelper.getString(R.string.default_error_message)
                    )
                )
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            firebaseToken.complete(FirebaseIdResult.Success(token))
        })

        return firebaseToken.await()

    }

}
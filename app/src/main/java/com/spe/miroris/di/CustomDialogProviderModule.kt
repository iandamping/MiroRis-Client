package com.spe.miroris.di

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.spe.miroris.databinding.CustomLoadingBinding
import com.spe.miroris.di.qualifier.CustomDialogQualifier
import com.spe.miroris.util.layoutInflater
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object CustomDialogProviderModule {

    @Provides
    @ActivityScoped
    @CustomDialogQualifier
    fun customDialog(@ActivityContext context: Context): AlertDialog {
        val customLoadingBinding: CustomLoadingBinding =
            CustomLoadingBinding.inflate(context.layoutInflater)
        customLoadingBinding.lottieAnimation.enableMergePathsForKitKatAndAbove(true)
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(customLoadingBinding.root)
        }
        return dialogBuilder.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

}
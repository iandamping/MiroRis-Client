package com.spe.miroris.core.presentation.helper

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface UtilityHelper {

    fun getDrawable(@DrawableRes resources: Int): Drawable?

    fun getString(@StringRes resources: Int): String
}
package com.spe.miroris.feature.addProduct.adapter

import android.net.Uri

sealed class MultiAdapterData {

    object Footer : MultiAdapterData()

    data class Main(val image: Uri) : MultiAdapterData()

}
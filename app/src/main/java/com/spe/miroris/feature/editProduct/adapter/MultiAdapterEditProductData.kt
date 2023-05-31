package com.spe.miroris.feature.editProduct.adapter

import android.net.Uri

sealed class MultiAdapterEditProductData {

    object Footer : MultiAdapterEditProductData()

    data class Main(val image: Uri) : MultiAdapterEditProductData()

}
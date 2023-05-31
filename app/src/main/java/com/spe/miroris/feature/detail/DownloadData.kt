package com.spe.miroris.feature.detail


sealed class DownloadData {
    data class Success(val msg: String) : DownloadData()
    data class Error(val msg: String) : DownloadData()
}
package com.spe.miroris.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

fun Activity.shareCacheDirBitmap(uri: Uri) {
    val fis = FileInputStream(uri.path) // 2nd line
    val bitmap = BitmapFactory.decodeStream(fis)
    fis.close()

    try {
        val file = File("${this.cacheDir}/receipt.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        Timber.e("Error in shareCacheDirBitmap : ${e.message}")
    }
}
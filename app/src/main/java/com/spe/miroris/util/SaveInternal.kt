package com.spe.miroris.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

// Extension method to save bitmap to internal storage
fun Bitmap.saveToInternalStorage(context: Context): Uri {
    // Get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // Initializing a new file
    // The bellow line return a directory in internal storage
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)

    // Create a file to save the image, random file name
    // file = File(file, "${UUID.randomUUID()}.png")

    file = File(file, "image.png")

    try {
        // Get the file output stream
        val stream: OutputStream = FileOutputStream(file)

        // Compress bitmap
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)

        // Flush the stream
        stream.flush()

        // Close stream
        stream.close()
    } catch (e: IOException) { // Catch the exception
        Timber.e("Error in saveToInternalStorage : ${e.message}")
    }

    // Return the saved image uri
    return Uri.parse(file.absolutePath)
}

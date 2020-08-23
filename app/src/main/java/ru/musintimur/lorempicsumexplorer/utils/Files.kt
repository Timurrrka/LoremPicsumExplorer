package ru.musintimur.lorempicsumexplorer.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveBitmapOnDisc(bitmap: Bitmap, context: Context, imageName: String, quality: Int = 100): Uri =
    runCatching {
        val directory = getImagesDirectory(context)
        val file = getFileInDir(directory, "$imageName.jpeg")

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        Uri.fromFile(file)
    }.getOrThrow()

private fun getImagesDirectory(context: Context): File {
    val directory = context.getDir("images", Context.MODE_PRIVATE)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    return directory
}

private fun getFileInDir(directory: File, name: String): File {
    val file = File(directory, name)
    if (file.exists()) {
        file.delete()
    }
    return file
}
package com.example.core_utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object ImageUtil {
    fun uriToFile(
        context: Context,
        uri: Uri
    ): File {
        val fileName = context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        OpenableColumns.DISPLAY_NAME
                    )
                )
            } else {
                null
            }
        } ?: "upload_file"

        val inputStream =
            context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open Uri")

        val file = File(
            context.cacheDir,
            fileName
        )

        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file
    }
}
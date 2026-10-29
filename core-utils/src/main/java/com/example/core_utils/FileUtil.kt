package com.example.core_utils

import java.util.Locale

object FileUtil {
    fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB")
        var sizeInDouble = size.toDouble()
        var unitIndex = 0

        while (sizeInDouble >= 1024 && unitIndex < units.lastIndex) {
            sizeInDouble /= 1024
            unitIndex++
        }

        return if (unitIndex == 0) {
            "${sizeInDouble.toLong()} ${units[unitIndex]}"
        } else {
            String.format(
                Locale.getDefault(),
                "%.1f %s",
                sizeInDouble,
                units[unitIndex]
            )
        }
    }
}
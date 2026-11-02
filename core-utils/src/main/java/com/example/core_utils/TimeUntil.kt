package com.example.core_utils

import java.util.Date

object TimeUntil {
    fun formatLastSeen(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - timestamp

        if (diff < 0) return ""

        val minutes = diff / 60_000
        val hours = diff / 3_600_000
        val days = diff / 86_400_000

        return when {
            days >= 7 -> ""
            minutes < 1 -> "Vừa truy cập"
            minutes < 60 -> "Hoạt động $minutes phút trước"
            hours < 24 -> "Hoạt động $hours giờ trước"
            else -> "Hoạt động $days ngày trước"
        }
    }
}
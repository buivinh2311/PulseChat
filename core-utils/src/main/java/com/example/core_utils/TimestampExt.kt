package com.example.core_utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Timestamp.toRelativeString(): String {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply { time = toDate() }

    val diffMillis = now.timeInMillis - target.timeInMillis
    val diffSeconds = diffMillis / 1000
    val diffMinutes = diffSeconds / 60
    val diffHours = diffMinutes / 60

    val isSameDay = now.get(Calendar.YEAR) == target.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterday.get(Calendar.YEAR) == target.get(Calendar.YEAR)
            && yesterday.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)

    return when {
        diffSeconds < 60 -> "Vừa xong"
        diffMinutes < 60 -> "$diffMinutes phút trước"
        isSameDay -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(toDate())
        isYesterday -> "Hôm qua"
        diffHours < 24 * 7 -> SimpleDateFormat("EEEE", Locale("vi")).format(toDate()) // Thứ Hai, Thứ Ba...
        now.get(Calendar.YEAR) == target.get(Calendar.YEAR) ->
            SimpleDateFormat("dd/MM", Locale.getDefault()).format(toDate())
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())
    }
}
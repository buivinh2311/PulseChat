package com.example.core_utils

import android.view.Window
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object SystemBarsUtil {
    fun hideSystemBars(window: Window) {
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showSystemBars(window: Window) {
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).show(WindowInsetsCompat.Type.systemBars())
    }
}
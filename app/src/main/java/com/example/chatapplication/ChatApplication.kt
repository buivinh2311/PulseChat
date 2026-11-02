package com.example.chatapplication

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication : Application() {

    @Inject
    lateinit var presenceLifecycleObserver: PresenceLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            presenceLifecycleObserver
        )
    }
}
package com.example.chatapplication

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.core_domain.repository.PresenceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenceLifecycleObserver @Inject constructor(
    private val presenceRepository: PresenceRepository
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            presenceRepository.startPresence()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            presenceRepository.stopPresence()
        }
    }
}
package com.example.feature_contacts.domain.model

import com.example.core_model.User

data class FriendWithOnlineStatus (
    val friend: User,
    val isOnline: Boolean
)
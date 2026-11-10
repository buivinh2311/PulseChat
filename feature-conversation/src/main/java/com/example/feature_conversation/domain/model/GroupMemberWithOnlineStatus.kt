package com.example.feature_conversation.domain.model

import com.example.core_model.User

data class GroupMemberWithOnlineStatus(
    val member: User,
    val isOnline: Boolean
)

package com.example.core_network.dto

import com.google.firebase.Timestamp

data class ConversationDto(
    val id: String = "",
    val type: String = "",
    val memberAvatars: List<String> = emptyList(),
    val participants: List<String> = emptyList(),
    val lastMessageActorId: String? = null,
    val lastMessageActorName: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: Timestamp? = null,
    val systemMessage: Boolean = false,
    val pinnedAt: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now()
)
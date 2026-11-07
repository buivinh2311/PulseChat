package com.example.core_model

import com.example.core_model.enum.ConversationType
import com.example.core_model.enum.MessageType
import com.google.firebase.Timestamp

data class Conversation(
    val id: String,
    val type: ConversationType,
    val participants: List<String>,
    val lastMessageActorId: String? = null,
    val lastMessageActorName: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: Timestamp? = null,
    val systemMessage: Boolean = false,
    val pinnedAt: Timestamp? = null,
    val createdAt: Timestamp
)
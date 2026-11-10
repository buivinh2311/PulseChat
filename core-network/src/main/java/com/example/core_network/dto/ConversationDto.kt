package com.example.core_network.dto

import com.example.core_model.enum.MessageType
import com.example.core_model.enum.SystemMessageType
import com.google.firebase.Timestamp

data class ConversationDto(
    val id: String = "",
    val type: String = "",
    val participants: List<String> = emptyList(),
    val lastMessageActorId: String? = null,
    val lastMessageActorName: String? = null,
    val lastMessageText: String? = null,
    val lastMessageTime: Timestamp? = null,
    val lastMessageType: MessageType? = null,
    val systemMessageType: SystemMessageType? = null,
    val pinnedAt: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now()
)
package com.example.infrastructure.mapper.remote

import com.example.core_model.Conversation
import com.example.core_model.enum.ConversationType
import com.example.core_network.dto.ConversationDto

fun ConversationDto.toDomain(): Conversation {
    return Conversation(
        id = id,
        type = runCatching {
            ConversationType.valueOf(type)
        }.getOrDefault(ConversationType.DIRECT),
        participants = participants,
        lastMessageActorId = lastMessageActorId,
        lastMessageActorName = lastMessageActorName,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageType = lastMessageType,
        systemMessageType = systemMessageType,
        pinnedAt = pinnedAt,
        createdAt = createdAt
    )
}

fun Conversation.toDto(): ConversationDto {
    return ConversationDto(
        id = id,
        type = type.name,
        participants = participants,
        lastMessageActorId = lastMessageActorId,
        lastMessageActorName = lastMessageActorName,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageType = lastMessageType,
        systemMessageType = systemMessageType,
        pinnedAt = pinnedAt,
        createdAt = createdAt
    )
}
package com.example.infrastructure.mapper.remote

import com.example.core_model.Group
import com.example.core_network.dto.GroupDto

fun GroupDto.toDomain(): Group {
    return Group(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        link = link,
        conversationId = conversationId,
        leaderId = leaderId,
        createdAt = createdAt
    )
}

fun Group.toDto(): GroupDto {
    return GroupDto(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        link = link,
        conversationId = conversationId,
        leaderId = leaderId,
        createdAt = createdAt
    )
}
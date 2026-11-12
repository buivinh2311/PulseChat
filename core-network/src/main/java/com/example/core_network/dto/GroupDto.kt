package com.example.core_network.dto

import com.google.firebase.Timestamp

data class GroupDto(
    val id: String = "",
    val name: String = "",
    val avatarUrl: String? = null,
    val link: String = "",
    val conversationId: String = "",
    val leaderId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
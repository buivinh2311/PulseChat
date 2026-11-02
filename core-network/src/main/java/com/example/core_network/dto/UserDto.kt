package com.example.core_network.dto

import com.google.firebase.Timestamp

data class UserDto(
    val id: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val coverPhotoUrl: String? = null,
    val fullName: String = "",
    val bio: String? = null,
    val dateOfBirth: Timestamp? = null,
    val gender: String? = null,
    val fcmTokens: List<String> = emptyList(),
    val createdAt: Timestamp? = Timestamp.now()
)
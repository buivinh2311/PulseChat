package com.example.core_model

import com.example.core_model.enum.Gender
import com.example.core_model.enum.UserStatus
import com.google.firebase.Timestamp


data class User(
    val id: String,
    val email: String,
    val avatarUrl: String? = null,
    val coverPhotoUrl: String? = null,
    val fullName: String,
    val bio: String? = null,
    val dateOfBirth: Timestamp? = null,
    val gender: Gender? = null,
    val fcmTokens: List<String> = emptyList(),
    val createdAt: Timestamp
)

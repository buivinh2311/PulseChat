package com.example.core_model.composite

import com.example.core_model.Friendship
import com.example.core_model.User

data class FriendshipWithUser (
    val friendship: Friendship,
    val friend: User
)
package com.example.feature_contacts.presentation.contacts

import com.example.core_model.Group
import com.example.core_model.User
import com.example.core_model.composite.FriendRequestWithUser
import com.example.feature_contacts.domain.model.FriendWithOnlineStatus

data class ContactsUiState(
    val currentUserId: String,
    val friends: List<FriendWithOnlineStatus>,
    val groups: List<Group>
)
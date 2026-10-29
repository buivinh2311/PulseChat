package com.example.feature_contacts.presentation.contacts

import com.example.core_model.Group
import com.example.core_model.User
import com.example.core_model.composite.FriendRequestWithUser

data class ContactsUiState(
    val currentUserId: String,
    val friends: List<User>,
    val groups: List<Group>
)
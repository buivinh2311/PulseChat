package com.example.feature_contacts.presentation.sentfriendrequest

import com.example.core_model.composite.FriendRequestWithUser

data class SentFriendRequestUiState(
    val friendRequestWithUsers: List<FriendRequestWithUser>
)
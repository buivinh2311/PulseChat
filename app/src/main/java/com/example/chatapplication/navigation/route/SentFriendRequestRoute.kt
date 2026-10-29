package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.core_ui.menu.AppBottomBarAction
import com.example.feature_contacts.presentation.sentfriendrequest.SentFriendRequestScreen

@Composable
fun SentFriendRequestRoute(
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    SentFriendRequestScreen(
        onBack = onBack,
        onBottomActionClick = onBottomActionClick
    )
}
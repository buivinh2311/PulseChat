package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.core_ui.menu.AppBottomBarAction
import com.example.feature_contacts.presentation.friendrequest.FriendRequestScreen

@Composable
fun FriendRequestRoute(
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    FriendRequestScreen(
        onBack = onBack,
        onBottomActionClick = onBottomActionClick
    )
}
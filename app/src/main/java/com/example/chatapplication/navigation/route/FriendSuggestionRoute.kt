package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.core_ui.menu.AppBottomBarAction
import com.example.feature_contacts.presentation.friendsuggestion.FriendSuggestionScreen

@Composable
fun FriendSuggestionRoute(
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    FriendSuggestionScreen(
        onBack = onBack,
        onBottomActionClick = onBottomActionClick
    )
}
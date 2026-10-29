package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.core_ui.menu.AppBottomBarAction
import com.example.feature_contacts.presentation.contacts.ContactsScreen

@Composable
fun ContactsRoute(
    onFriendRequest: () -> Unit,
    onFriendSent: () -> Unit,
    onFriendSuggestion: () -> Unit,
    onContact: (String) -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    ContactsScreen(
        onFriendRequest = onFriendRequest,
        onFriendSent = onFriendSent,
        onFriendSuggestion = onFriendSuggestion,
        onContact = onContact,
        onBottomActionClick = onBottomActionClick
    )
}
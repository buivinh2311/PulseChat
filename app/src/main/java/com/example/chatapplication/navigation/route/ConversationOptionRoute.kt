package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.feature_conversation.presentation.conversationoption.ConversationOptionScreen

@Composable
fun ConversationOptionRoute(
    conversationId: String,
    onBack: () -> Unit,
    onCreateGroup: (String) -> Unit,
    onViewMember: (String) -> Unit,
    onGroupLink: (String) -> Unit
) {
    ConversationOptionScreen(
        conversationId = conversationId,
        onBack = onBack,
        onCreateGroup = onCreateGroup,
        onViewMember = onViewMember,
        onGroupLink = onGroupLink
    )
}
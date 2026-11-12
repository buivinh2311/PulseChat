package com.example.chatapplication.navigation.route

import androidx.compose.runtime.Composable
import com.example.feature_conversation.presentation.grouplink.GroupLinkScreen

@Composable
fun GroupLinkRoute(
    conversationId: String,
    onBack: () -> Unit
) {
    GroupLinkScreen(
        conversationId = conversationId,
        onBack = onBack
    )
}
package com.example.feature_conversation.presentation.conversationdetail

import com.example.core_model.Message
import com.example.core_model.User

data class ConversationDetailUiState(
    val conversationDetailData: ConversationDetailData,
    val currentUser: User,
    val isConnected: Boolean,
    val isSentRequest: Boolean,
    val messages: List<Message>,
    val pinnedMessages: List<Message>,
    val isOnline: Boolean,
    val lastSeen: Long? = null
)
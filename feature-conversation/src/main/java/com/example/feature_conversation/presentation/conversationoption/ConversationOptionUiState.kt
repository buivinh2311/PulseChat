package com.example.feature_conversation.presentation.conversationoption

import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData

data class ConversationOptionUiState(
    val conversationDetailData: ConversationDetailData,
    val currentUserId: String
)
package com.example.core_model.composite

import com.example.core_model.Conversation

data class ConversationWithInfo (
    val conversation: Conversation,
    val displayInfo: ConversationDisplayInfo,
    val isOnline: Boolean = false
)
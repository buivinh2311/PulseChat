package com.example.core_utils

object ConversationIdUtil {
    fun buildDirectConversationId(
        currentUserId: String,
        friendId: String
    ): String {
        require(currentUserId.isNotBlank())
        require(friendId.isNotBlank())
        require(currentUserId != friendId)

        return listOf(currentUserId, friendId)
            .sorted()
            .joinToString("_")
    }
}
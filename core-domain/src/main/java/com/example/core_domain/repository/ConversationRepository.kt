package com.example.core_domain.repository

import com.example.core_model.Conversation
import com.example.core_model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun getConversationById(conversationId: String): Result<Conversation?>

    fun observerConversationById(conversationId: String): Flow<Conversation?>

    fun observeConversationsByUserId(userId: String): Flow<List<Conversation>>

    suspend fun getConversationGroupsByUserId(userId: String): Result<List<Conversation>>

    suspend fun ensureConversation(user1: FirebaseUser, user2: User): Result<String>

    fun getConversationIdByFriendship(
        currentUserId: String,
        friendId: String
    ): String

    suspend fun pinConversation(conversationId: String): Result<Unit>

    suspend fun unpinConversation(conversationId: String): Result<Unit>
}
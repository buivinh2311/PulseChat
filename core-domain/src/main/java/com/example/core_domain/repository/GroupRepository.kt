package com.example.core_domain.repository

import com.example.core_model.Conversation
import com.example.core_model.Group
import com.example.core_model.User
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun createGroup(
        name: String,
        avatarUrl: String? = null,
        conversationId: String,
        leaderId: String
    ): Result<Unit>

    suspend fun getGroupByConversationId(conversationId: String): Result<Group?>

    suspend fun getGroupsByConversationIds(conversationIds: List<String>): Result<List<Group>>

    fun observerGroupByConversationId(conversationId: String): Flow<Group?>

    suspend fun changeAvatar(groupId: String, avatarUrl: String): Result<Unit>

    suspend fun changeGroupName(groupId: String, groupName: String): Result<Unit>

}
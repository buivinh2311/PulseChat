package com.example.infrastructure.repository

import android.util.Log
import com.example.core_domain.repository.GroupRepository
import com.example.core_model.Conversation
import com.example.core_model.Group
import com.example.core_model.User
import com.example.core_model.enum.ConversationType
import com.example.core_network.dto.ConversationDto
import com.example.core_network.dto.GroupDto
import com.example.infrastructure.mapper.remote.toDomain
import com.example.infrastructure.mapper.remote.toDto
import com.example.infrastructure.repository.ConversationRepositoryImpl.Companion.CONVERSATIONS
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GroupRepository {
    companion object {
        const val GROUPS = "groups"
    }

    override suspend fun createGroup(
        conversation: Conversation,
        group: Group
    ): Result<String> {
        return try {
            val batch = firestore.batch()

            val conversationRef = firestore
                .collection("conversations")
                .document()

            val groupRef = firestore
                .collection(GROUPS)
                .document()

            val conversationDto = conversation.copy(
                id = conversationRef.id
            ).toDto()

            val groupDto = group.copy(
                id = groupRef.id,
                conversationId = conversationRef.id
            ).toDto()

            batch.set(conversationRef, conversationDto)
            batch.set(groupRef, groupDto)
            batch.commit().await()

            Result.success(conversationRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupByConversationId(
        conversationId: String
    ): Result<Group?> {
        return try {
            val group = firestore
                .collection(GROUPS)
                .whereEqualTo("conversationId", conversationId)
                .get()
                .await()
                .firstOrNull()
                ?.toObject(GroupDto::class.java)
                ?.toDomain()
            Result.success(group)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupsByConversationIds(
        conversationIds: List<String>
    ): Result<List<Group>> {
        return try {
            val groups = firestore
                .collection(GROUPS)
                .whereIn("conversationId", conversationIds)
                .get()
                .await()
                .mapNotNull {
                    it.toObject(GroupDto::class.java).toDomain()
                }
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observerGroupByConversationId(
        conversationId: String
    ): Flow<Group?> = callbackFlow {
        val listener = firestore
            .collection(GROUPS)
            .whereEqualTo("conversationId", conversationId)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val group = snapshots
                    ?.documents
                    ?.firstOrNull()
                    ?.toObject(GroupDto::class.java)
                    ?.toDomain()
                trySend(group)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun changeAvatar(
        groupId: String,
        avatarUrl: String
    ): Result<Unit> {
        return try {
            firestore
                .collection(GROUPS)
                .document(groupId)
                .update("avatarUrl", avatarUrl)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeGroupName(
        groupId: String,
        groupName: String
    ): Result<Unit> {
        return try {
            firestore
                .collection(GROUPS)
                .document(groupId)
                .update("name", groupName)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
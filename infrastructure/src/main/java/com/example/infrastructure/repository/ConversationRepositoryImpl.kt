package com.example.infrastructure.repository

import android.util.Log
import com.example.core_domain.repository.ConversationRepository
import com.example.core_model.Conversation
import com.example.core_model.User
import com.example.core_model.enum.ConversationType
import com.example.core_network.dto.ConversationDto
import com.example.infrastructure.mapper.remote.toDomain
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ConversationRepository {
    companion object {
        const val CONVERSATIONS = "conversations"
    }

    private fun buildConversationId(
        user1: String,
        user2: String
    ): String {
        return listOf(user1, user2)
            .sorted()
            .joinToString("_")
    }

    override suspend fun getConversationById(
        conversationId: String
    ): Result<Conversation?> {
        return try {
            val conversation = firestore
                .collection(CONVERSATIONS)
                .document(conversationId)
                .get()
                .await()
                ?.toObject(ConversationDto::class.java)
                ?.toDomain()
            Result.success(conversation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observerConversationById(
        conversationId: String
    ): Flow<Conversation?> = callbackFlow {
        val listener = firestore
            .collection(CONVERSATIONS)
            .document(conversationId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val conversation = snapshot
                    ?.toObject(ConversationDto::class.java)
                    ?.toDomain()
                trySend(conversation)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun observeConversationsByUserId(
        userId: String
    ): Flow<List<Conversation>> = callbackFlow {
        val listener = firestore
            .collection(CONVERSATIONS)
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val conversations = snapshots?.documents
                    ?.mapNotNull {
                        it.toObject(ConversationDto::class.java)?.toDomain()
                    }
                    ?.sortedWith(
                        compareByDescending<Conversation> { it.pinnedAt != null }
                            .thenByDescending { it.pinnedAt }
                            .thenByDescending { it.lastMessageTime }
                    )
                    ?: emptyList()

                trySend(conversations)
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getConversationGroupsByUserId(
        userId: String
    ): Result<List<Conversation>> {
        return try {
            val conversations = firestore.collection(CONVERSATIONS)
                .whereEqualTo("type", "GROUP")
                .whereArrayContains("participants", userId)
                .get()
                .await()
                .mapNotNull {
                    it.toObject(ConversationDto::class.java).toDomain()
                }
            Result.success(conversations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ensureConversation(
        user1: FirebaseUser,
        user2: User
    ): Result<String> {
        return try {
            val conversationId = buildConversationId(user1.uid, user2.id)
            val ref = firestore
                .collection(CONVERSATIONS)
                .document(conversationId)
            if (!ref.get().await().exists()) {
                ref.set(
                    ConversationDto(
                        id = conversationId,
                        type = ConversationType.DIRECT.name,
                        memberAvatars = listOf(user1.photoUrl.toString(), user2.id),
                        participants = listOf(user1.uid, user2.id),
                        createdAt = Timestamp.now()
                    )
                ).await()
            }
            Result.success(conversationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createConversation(
        currentUser: FirebaseUser,
        otherUser: List<User>
    ): Result<String> {
        return try {
            val memberAvatars = buildList {
                currentUser.photoUrl?.toString()?.let { add(it) }
                addAll(otherUser.mapNotNull { it.avatarUrl })
            }

            val participants = buildList {
                add(currentUser.uid)
                addAll(otherUser.map { it.id })
            }

            val type = if (otherUser.size < 2) {
                ConversationType.DIRECT
            } else {
                ConversationType.GROUP
            }

            val messageRef = firestore
                .collection(CONVERSATIONS)
                .document()
            firestore
                .collection(CONVERSATIONS)
                .document()
                .set(
                    ConversationDto(
                        id = messageRef.id,
                        type = type.name,
                        memberAvatars = memberAvatars,
                        participants = participants,
                        createdAt = Timestamp.now()
                    )
                ).await()
            Result.success(messageRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getConversationIdByFriendship(
        currentUserId: String,
        friendId: String
    ): String {
        return buildConversationId(currentUserId, friendId)
    }

    override suspend fun pinConversation(conversationId: String): Result<Unit> {
        return try {
            firestore.collection(CONVERSATIONS)
                .document(conversationId)
                .update("pinnedAt", Timestamp.now())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unpinConversation(conversationId: String): Result<Unit> {
        return try {
            firestore.collection(CONVERSATIONS)
                .document(conversationId)
                .update("pinnedAt", null)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
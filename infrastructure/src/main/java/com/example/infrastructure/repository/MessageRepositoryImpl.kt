package com.example.infrastructure.repository

import android.content.Context
import com.example.core_domain.repository.MessageRepository
import com.example.core_model.Message
import com.example.core_network.dto.MessageDto
import com.example.core_utils.FirestoreCollectionsUtil
import com.example.infrastructure.mapper.remote.toDomain
import com.example.infrastructure.util.addMessageAndUpdateConversation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.collections.mapOf
import kotlin.collections.mutableMapOf

class MessageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MessageRepository {
    private fun messagesRef(conversationId: String) = firestore
        .collection(FirestoreCollectionsUtil.CONVERSATIONS)
        .document(conversationId)
        .collection(FirestoreCollectionsUtil.MESSAGES)

    override fun observeMessagesByConversationId(
        conversationId: String,
        pageSize: Int
    ): Flow<List<Message>> = callbackFlow {
        val listener = messagesRef(conversationId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(pageSize.toLong())
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val messages = snapshots?.documents
                    ?.mapNotNull {
                        it.toObject(MessageDto::class.java)?.copy(id = it.id)?.toDomain()
                    }
                    ?: emptyList()
                trySend(messages.reversed())
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getMessageById(
        conversationId: String,
        messageId: String
    ): Result<Message?> {
        return try {
            val message = messagesRef(conversationId)
                .document(messageId)
                .get()
                .await()
                ?.toObject(MessageDto::class.java)
                ?.toDomain()
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadMoreMessagesByConversationId(
        conversationId: String,
        lastVisibleTimestamp: Timestamp,
        pageSize: Int
    ): Result<List<Message>> {
        return try {
            val snapshot = messagesRef(conversationId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .startAfter(lastVisibleTimestamp)
                .limit(pageSize.toLong())
                .get()
                .await()

            Result.success(
                snapshot.documents.mapNotNull {
                    it.toObject(MessageDto::class.java)?.copy(id = it.id)?.toDomain()
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(
        senderName: String,
        message: Message,
        targetUserName: String?
    ): Result<Unit> {
        return try {
            val conversationRef = firestore
                .collection(FirestoreCollectionsUtil.CONVERSATIONS)
                .document(message.conversationId)

            val messageRef = conversationRef
                .collection(FirestoreCollectionsUtil.MESSAGES)
                .document()

            firestore.runBatch { batch ->
                batch.addMessageAndUpdateConversation(
                    conversationRef = conversationRef,
                    messageRef = messageRef,
                    message = message,
                    actorUserName = senderName
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(
        conversationId: String,
        messageId: String,
        uid: String
    ): Result<Unit> {
        return try {
            messagesRef(conversationId).document(messageId)
                .update("readBy", FieldValue.arrayUnion(uid))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reactToMessage(
        conversationId: String,
        messageId: String,
        userId: String,
        emoji: String
    ): Result<Unit> {
        return try {
            val messageRef = messagesRef(conversationId).document(messageId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(messageRef)
                val reactions = mutableMapOf<String, String>()
                val data = snapshot.get("reactions") as? Map<*, *>

                data?.forEach { (key, value) ->
                    if (key is String && value is String) {
                        reactions[key] = value
                    }
                }

                val currentReaction = reactions[userId]
                if (currentReaction == emoji) {
                    reactions.remove(userId)
                } else {
                    reactions[userId] = emoji
                }

                transaction.update(
                    messageRef,
                    "reactions",
                    reactions
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeReaction(
        conversationId: String,
        messageId: String,
        userId: String
    ): Result<Unit> {
        return try {
            val messageRef = messagesRef(conversationId).document(messageId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(messageRef)
                val reactions = mutableMapOf<String, String>()

                (snapshot.get("reactions") as? Map<*, *>)?.forEach { (key, value) ->
                    if (key is String && value is String) {
                        reactions[key] = value
                    }
                }

                reactions.remove(userId)

                transaction.update(
                    messageRef,
                    "reactions",
                    reactions
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun pinMessage(
        conversationId: String,
        messageId: String,
        pinnedByUserId: String,
        pinnedAt: Timestamp
    ): Result<Unit> {
        return try {
            messagesRef(conversationId)
                .document(messageId)
                .set(
                    mapOf(
                        "pinnedByUserId" to pinnedByUserId,
                        "pinnedAt" to pinnedAt
                    ),
                    SetOptions.merge()
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observePinnedMessage(
        conversationId: String
    ): Flow<List<Message>> = callbackFlow {
        val listener = messagesRef(conversationId)
            .whereNotEqualTo("pinnedByUserId", null)
            .orderBy("pinnedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents
                    ?.mapNotNull {
                        it.toObject(MessageDto::class.java)?.copy(id = it.id)?.toDomain()
                    }
                    ?: emptyList()
                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun unpinMessage(
        conversationId: String,
        messageId: String
    ): Result<Unit> {
        return try {
            messagesRef(conversationId)
                .document(messageId)
                .set(
                    mapOf(
                        "pinnedByUserId" to FieldValue.delete(),
                        "pinnedAt" to FieldValue.delete()
                    ),
                    SetOptions.merge()
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
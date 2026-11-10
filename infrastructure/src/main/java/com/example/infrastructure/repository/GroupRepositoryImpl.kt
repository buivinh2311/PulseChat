package com.example.infrastructure.repository

import android.content.Context
import android.util.Log
import com.example.core_domain.repository.GroupRepository
import com.example.core_model.Conversation
import com.example.core_model.Group
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_model.enum.MessageStatus
import com.example.core_model.enum.SystemMessageType
import com.example.core_network.dto.GroupDto
import com.example.core_utils.FirestoreCollectionsUtil
import com.example.infrastructure.mapper.remote.toDomain
import com.example.infrastructure.mapper.remote.toDto
import com.example.infrastructure.util.addMessageAndUpdateConversation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore
) : GroupRepository {
    override suspend fun createGroup(
        conversation: Conversation,
        group: Group,
        members: List<User>,
        actorUserName: String
    ): Result<String> {
        return try {
            val conversationRef = firestore
                .collection(FirestoreCollectionsUtil.CONVERSATIONS)
                .document()

            val groupRef = firestore
                .collection(FirestoreCollectionsUtil.GROUPS)
                .document()

            val conversationDto = conversation.copy(
                id = conversationRef.id
            ).toDto()

            val groupDto = group.copy(
                id = groupRef.id,
                conversationId = conversationRef.id
            ).toDto()

            firestore.runBatch { batch ->
                batch.set(conversationRef, conversationDto)
                batch.set(groupRef, groupDto)
                members.forEach { member ->
                    val messageRef = conversationRef
                        .collection(FirestoreCollectionsUtil.MESSAGES)
                        .document()

                    val systemMessage = Message.System(
                        id = "",
                        conversationId = conversationRef.id,
                        senderId = group.leaderId,
                        status = MessageStatus.DELIVERED,
                        createdAt = Timestamp.now(),
                        systemMessageType = if(group.leaderId == member.id) {
                            SystemMessageType.CREATE_GROUP
                        } else {
                            SystemMessageType.ADD_GROUP_MEMBER
                        },
                        text = member.fullName,
                        targetUserId = member.id
                    )

                    batch.addMessageAndUpdateConversation(
                        conversationRef = conversationRef,
                        messageRef = messageRef,
                        message = systemMessage,
                        actorUserName = actorUserName
                    )
                }
            }.await()
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
                .collection(FirestoreCollectionsUtil.GROUPS)
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
                .collection(FirestoreCollectionsUtil.GROUPS)
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
            .collection(FirestoreCollectionsUtil.GROUPS)
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
        group: Group,
        avatarUrl: String,
        systemMessage: Message.System,
        actorUserName: String
    ): Result<Unit> {
        return try {
            val groupRef = firestore
                .collection(FirestoreCollectionsUtil.GROUPS)
                .document(group.id)

            val conversationRef = firestore
                .collection(FirestoreCollectionsUtil.CONVERSATIONS)
                .document(group.conversationId)

            val messageRef = conversationRef
                .collection(FirestoreCollectionsUtil.MESSAGES)
                .document()

            firestore.runBatch { batch ->
                batch.update(groupRef, "avatarUrl", avatarUrl)
                batch.addMessageAndUpdateConversation(
                    conversationRef = conversationRef,
                    messageRef = messageRef,
                    message = systemMessage,
                    actorUserName = actorUserName
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeGroupName(
        group: Group,
        groupName: String,
        systemMessage: Message.System,
        actorUserName: String
    ): Result<Unit> {
        return try {
            val groupRef = firestore
                .collection(FirestoreCollectionsUtil.GROUPS)
                .document(group.id)

            val conversationRef = firestore
                .collection(FirestoreCollectionsUtil.CONVERSATIONS)
                .document(group.conversationId)

            val messageRef = conversationRef
                .collection(FirestoreCollectionsUtil.MESSAGES)
                .document()

            firestore.runBatch { batch ->
                batch.update(groupRef, "name", groupName)
                batch.addMessageAndUpdateConversation(
                    conversationRef = conversationRef,
                    messageRef = messageRef,
                    message = systemMessage,
                    actorUserName = actorUserName
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
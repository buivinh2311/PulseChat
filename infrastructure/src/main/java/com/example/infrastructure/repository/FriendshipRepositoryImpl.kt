package com.example.infrastructure.repository

import com.example.core_domain.repository.FriendshipRepository
import com.example.core_model.Friendship
import com.example.core_network.dto.FriendDto
import com.example.core_utils.FirestoreCollectionsUtil
import com.example.infrastructure.mapper.remote.toDomain
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendshipRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): FriendshipRepository {
    private fun buildFriendId(
        user1: String,
        user2: String
    ): String {
        return listOf(user1, user2)
            .sorted()
            .joinToString("_")
    }

    override fun createFriendship(
        transaction: Transaction,
        user1: String,
        user2: String
    ): Result<Unit> {
        return try {
            val friendId = buildFriendId(user1, user2)
            val friend = FriendDto(
                id = friendId,
                participants = listOf(user1, user2),
                createdAt = Timestamp.now()
            )

            val friendRef = firestore
                .collection(FirestoreCollectionsUtil.FRIENDSHIPS)
                .document(friendId)

            transaction.set(friendRef, friend)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeFriendship(
        user1: String,
        user2: String
    ): Flow<Boolean> = callbackFlow {
        val friendId = buildFriendId(user1, user2)
        val listener = firestore
            .collection(FirestoreCollectionsUtil.FRIENDSHIPS)
            .document(friendId)
            .addSnapshotListener { snapshot, exception ->
                if(exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() == true)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun observeFriendshipsByUserId(
        uid: String
    ): Flow<List<Friendship>> = callbackFlow {
        val listener = firestore
            .collection(FirestoreCollectionsUtil.FRIENDSHIPS)
            .whereArrayContains("participants", uid)
            .addSnapshotListener { snapshots, exception ->
                if(exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val friends = snapshots?.documents
                    ?.mapNotNull {
                        it.toObject(FriendDto::class.java)
                            ?.toDomain()
                    }
                    ?: emptyList()
                trySend(friends)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getFriendIdsByUserId(uid: String): Result<List<String>> {
        return try {
            val friendIds = firestore
                .collection(FirestoreCollectionsUtil.FRIENDSHIPS)
                .whereArrayContains("participants", uid)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.toObject(FriendDto::class.java)
                        ?.participants
                        ?.firstOrNull{ participateId -> participateId != uid}
                }
            Result.success(friendIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unFriend(friendId: String): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.FRIENDSHIPS)
                .document(friendId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
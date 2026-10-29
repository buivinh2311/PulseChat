package com.example.infrastructure.repository

import android.util.Log
import com.example.core_domain.repository.FriendRequestRepository
import com.example.core_model.FriendRequest
import com.example.core_model.enum.FriendRequestStatus
import com.example.core_network.dto.FriendRequestDto
import com.example.infrastructure.mapper.remote.toDomain
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRequestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): FriendRequestRepository {
    private companion object {
        const val FRIEND_REQUESTS = "friend_requests"
    }

    private fun buildFriendRequestId(
        senderId: String,
        receiverId: String
    ): String {
        return listOf(senderId, receiverId)
            .sorted()
            .joinToString("_")
    }

    override suspend fun sendFriendRequest(
        senderId: String,
        receiverId: String
    ): Result<Unit> {
        return try {
            val request = FriendRequestDto(
                senderId = senderId,
                receiverId = receiverId,
                status = FriendRequestStatus.PENDING.name,
                createdAt = Timestamp.now()
            )
            val friendRequestId = buildFriendRequestId(senderId, receiverId)
            firestore
                .collection(FRIEND_REQUESTS)
                .document(friendRequestId)
                .set(request)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeSentFriendRequest(
        senderId: String,
        receiverId: String
    ): Flow<Boolean> = callbackFlow {
        val friendRequestId = buildFriendRequestId(senderId, receiverId)
        val listener = firestore
            .collection(FRIEND_REQUESTS)
            .document(friendRequestId)
            .addSnapshotListener { snapshots, exception ->
                if(exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                trySend(snapshots?.exists() == true)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun observeFriendRequestsByReceiverId(
        uid: String
    ): Flow<List<FriendRequest>> = callbackFlow {
        val listener = firestore
            .collection(FRIEND_REQUESTS)
            .whereEqualTo("receiverId", uid)
            .whereEqualTo(
                "status",
                FriendRequestStatus.PENDING.name
            )
            .orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            )
            .addSnapshotListener { snapshots, exception ->
                if(exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val list = snapshots?.documents
                    ?.mapNotNull {
                        it.toObject(FriendRequestDto::class.java)
                            ?.toDomain(it.id)
                    }
                    ?: emptyList()
                trySend(list)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun observeFriendRequestsBySenderId(
        senderId: String
    ): Flow<List<FriendRequest>> = callbackFlow {
        val listener = firestore
            .collection(FRIEND_REQUESTS)
            .whereEqualTo("senderId", senderId)
            .whereEqualTo(
                "status",
                FriendRequestStatus.PENDING.name
            )
            .orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            )
            .addSnapshotListener { snapshots, exception ->
                if(exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val list = snapshots?.documents
                    ?.mapNotNull {
                        it.toObject(FriendRequestDto::class.java)
                            ?.toDomain(it.id)
                    }
                    ?: emptyList()
                trySend(list)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun rejectRequest(requestId: String): Result<Unit> {
        return try {
            firestore
                .collection(FRIEND_REQUESTS)
                .document(requestId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun deleteRequest(
        transaction: Transaction,
        requestId: String
    ): Result<Unit> {
        return try {
            val requestRef = firestore
                .collection(FRIEND_REQUESTS)
                .document(requestId)
            transaction.delete(requestRef)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun recallRequest(request: FriendRequest): Result<Unit> {
        return try {
            firestore
                .collection(FRIEND_REQUESTS)
                .document(request.id)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.example.core_domain.repository

import com.example.core_model.FriendRequest
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.Flow

interface FriendRequestRepository {
    suspend fun sendFriendRequest(senderId: String, receiverId: String): Result<Unit>
    fun observeSentFriendRequest(senderId: String, receiverId: String): Flow<Boolean>
    fun observeFriendRequestsByReceiverId(uid: String): Flow<List<FriendRequest>>
    fun observeFriendRequestsBySenderId(senderId: String): Flow<List<FriendRequest>>
    suspend fun rejectRequest(requestId: String): Result<Unit>
    fun deleteRequest(
        transaction: Transaction,
        requestId: String
    ): Result<Unit>
    suspend fun recallRequest(request: FriendRequest): Result<Unit>
}
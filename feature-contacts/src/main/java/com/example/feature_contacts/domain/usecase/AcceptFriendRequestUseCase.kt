package com.example.feature_contacts.domain.usecase

import com.example.core_domain.repository.FriendshipRepository
import com.example.core_domain.repository.FriendRequestRepository
import com.example.core_model.FriendRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val friendshipRepository: FriendshipRepository,
    private val friendRequestRepository: FriendRequestRepository
) {
    suspend operator fun invoke(
        request: FriendRequest
    ): Result<Unit> = runCatching {
        firestore.runTransaction { transaction ->
            friendshipRepository.createFriendship(
                transaction,
                request.senderId,
                request.receiverId
            )

            friendRequestRepository.deleteRequest(
                transaction,
                request.id
            )
            null
        }.await()
    }
}
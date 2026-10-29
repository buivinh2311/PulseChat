package com.example.feature_contacts.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.FriendRequestRepository
import com.example.core_domain.repository.UserRepository
import com.example.core_model.composite.FriendRequestWithUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFriendRequestsByReceiverIdUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val friendRequestRepository: FriendRequestRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<FriendRequestWithUser>> = flow{
        val uid = authRepository.getCurrentUserId()
            ?: throw IllegalStateException("User not logged in")

        emitAll(
            friendRequestRepository.observeFriendRequestsByReceiverId(uid).map { requests ->
                val senderIds = requests
                    .map { it.senderId }
                    .distinct()

                val users = userRepository
                    .getUsersByIds(senderIds)
                    .getOrElse { throw Exception("Get Users failed") }

                val userMap = users.associateBy { it.id }

                requests.mapNotNull { request ->
                    val user = userMap[request.senderId] ?: return@mapNotNull null
                    FriendRequestWithUser(
                        request = request,
                        user = user
                    )
                }
            }
        )
    }
}
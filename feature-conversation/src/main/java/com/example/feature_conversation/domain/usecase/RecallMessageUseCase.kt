package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.MessageRepository
import javax.inject.Inject

class RecallMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
}
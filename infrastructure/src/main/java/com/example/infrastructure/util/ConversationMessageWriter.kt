package com.example.infrastructure.util

import android.content.Context
import com.example.core_model.Message
import com.example.core_model.enum.MessageType
import com.example.core_model.enum.SystemMessageType
import com.example.core_resource.R
import com.example.infrastructure.mapper.remote.toDto
import com.example.infrastructure.mapper.remote.toFirestoreMap
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import dagger.hilt.android.qualifiers.ApplicationContext

fun WriteBatch.addMessageAndUpdateConversation(
    conversationRef: DocumentReference,
    messageRef: DocumentReference,
    message: Message,
    actorUserName: String
) {
    val messageDto = message.toDto(messageRef.id)
    set(messageRef, messageDto.toFirestoreMap())
    update(
        conversationRef,
        mapOf(
            "lastMessageText" to when(message) {
                is Message.Text -> message.text
                is Message.File -> message.fileName
                is Message.System -> message.text
                else -> null
            },
            "lastMessageActorId" to message.senderId,
            "lastMessageActorName" to actorUserName,
            "lastMessageTime" to message.createdAt,
            "lastMessageType" to when (message) {
                is Message.Text -> MessageType.TEXT.name
                is Message.Image -> MessageType.IMAGE.name
                is Message.Video -> MessageType.VIDEO.name
                is Message.File -> MessageType.FILE.name
                is Message.System -> MessageType.SYSTEM.name
            },
            "systemMessageType" to when(message) {
                is Message.System -> message.systemMessageType.name
                else -> null
            }
        )
    )
}
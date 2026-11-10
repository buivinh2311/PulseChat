package com.example.core_model

import com.example.core_model.enum.MessageStatus
import com.example.core_model.enum.SystemMessageType
import com.google.firebase.Timestamp

sealed class Message {
    abstract val id: String
    abstract val conversationId: String
    abstract val senderId: String
    abstract val status: MessageStatus
    abstract val createdAt: Timestamp
//    abstract val recalledAt: Timestamp
    abstract val reactions: Map<String, String>


    data class Text(
        override val id: String,
        override val conversationId: String,
        override val senderId: String,
        override val status: MessageStatus,
        override val createdAt: Timestamp,
        override val reactions: Map<String, String> = emptyMap(),
        val replyPreview: ReplyPreview? = null,
        val pinnedByUserId: String? = null,
        val pinnedAt: Timestamp? = null,
        val text: String
    ) : Message()

    data class Image(
        override val id: String,
        override val conversationId: String,
        override val senderId: String,
        override val status: MessageStatus,
        override val createdAt: Timestamp,
        override val reactions: Map<String, String> = emptyMap(),
        val replyPreview: ReplyPreview? = null,
        val pinnedByUserId: String? = null,
        val pinnedAt: Timestamp? = null,
        val fileUrl: String,
        val fileName: String,
        val fileSize: Long,
        val width: Int,
        val height: Int,
        val mimeType: String
    ) : Message()

    data class Video(
        override val id: String,
        override val conversationId: String,
        override val senderId: String,
        override val status: MessageStatus,
        override val createdAt: Timestamp,
        override val reactions: Map<String, String> = emptyMap(),
        val replyPreview: ReplyPreview? = null,
        val pinnedByUserId: String? = null,
        val pinnedAt: Timestamp? = null,
        val fileUrl: String,
        val thumbnailUrl: String,
        val fileName: String,
        val fileSize: Long,
        val duration: Long,
        val width: Int,
        val height: Int,
        val mimeType: String
    ) : Message()

    data class File(
        override val id: String,
        override val conversationId: String,
        override val senderId: String,
        override val status: MessageStatus,
        override val createdAt: Timestamp,
        override val reactions: Map<String, String> = emptyMap(),
        val replyPreview: ReplyPreview? = null,
        val pinnedByUserId: String? = null,
        val pinnedAt: Timestamp? = null,
        val fileUrl: String,
        val thumbnailUrl: String?,
        val fileName: String,
        val fileSize: Long,
        val width: Int,
        val height: Int,
        val mimeType: String
    ) : Message()

    data class System(
        override val id: String,
        override val conversationId: String,
        override val senderId: String,
        override val status: MessageStatus,
        override val createdAt: Timestamp,
        override val reactions: Map<String, String> = emptyMap(),
        val systemMessageType: SystemMessageType,
        val text: String? = null,
        val targetMessageId: String? = null,
        val targetUserId: String? = null
    ) : Message()
}
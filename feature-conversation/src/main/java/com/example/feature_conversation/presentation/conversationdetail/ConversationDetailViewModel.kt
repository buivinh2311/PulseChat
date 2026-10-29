package com.example.feature_conversation.presentation.conversationdetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_model.FriendRequest
import com.example.core_model.ReplyPreview
import com.example.core_model.User
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.model.FileInfo
import com.example.feature_conversation.domain.usecase.DownloadFileUseCase
import com.example.feature_conversation.domain.usecase.DownloadImageUseCase
import com.example.feature_conversation.domain.usecase.DownloadVideoUseCase
import com.example.feature_conversation.domain.usecase.GetConversationByIdUseCase
import com.example.core_domain.usecase.GetCurrentUserUseCase
import com.example.feature_conversation.domain.usecase.LoadMoreMessagesUseCase
import com.example.feature_conversation.domain.usecase.MarkMessageAsReadUseCase
import com.example.feature_conversation.domain.usecase.ObserveFriendRequestUseCase
import com.example.feature_conversation.domain.usecase.ObserveFriendshipUseCase
import com.example.feature_conversation.domain.usecase.ObserveMessagesByConversationIdUseCase
import com.example.feature_conversation.domain.usecase.ObservePinnedMessageUseCase
import com.example.feature_conversation.domain.usecase.OpenFileUseCase
import com.example.feature_conversation.domain.usecase.PinMessageUseCase
import com.example.feature_conversation.domain.usecase.ReactToMessageUseCase
import com.example.feature_conversation.domain.usecase.RecallFriendRequestUseCase
import com.example.feature_conversation.domain.usecase.RemoveReactionUseCase
import com.example.feature_conversation.domain.usecase.ReplyMessageUseCase
import com.example.feature_conversation.domain.usecase.SendFileMessageUseCase
import com.example.feature_conversation.domain.usecase.SendFriendRequestUseCase
import com.example.feature_conversation.domain.usecase.SendImageMessageUseCase
import com.example.feature_conversation.domain.usecase.SendTextMessageUseCase
import com.example.feature_conversation.domain.usecase.SendVideoMessageUseCase
import com.example.feature_conversation.domain.usecase.UnpinMessageUseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onFailure

@HiltViewModel
class ConversationDetailViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeFriendRequestUseCase: ObserveFriendRequestUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getConversationByIdUseCase: GetConversationByIdUseCase,
    private val recallFriendRequestUseCase: RecallFriendRequestUseCase,
    private val observeFriendshipUseCase: ObserveFriendshipUseCase,
    private val observeMessagesByConversationIdUseCase: ObserveMessagesByConversationIdUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
    private val sendTextMessageUseCase: SendTextMessageUseCase,
    private val sendImageMessageUseCase: SendImageMessageUseCase,
    private val sendVideoMessageUseCase: SendVideoMessageUseCase,
    private val sendFileMessageUseCase: SendFileMessageUseCase,
    private val markMessageAsReadUseCase: MarkMessageAsReadUseCase,
    private val downloadImageUseCase: DownloadImageUseCase,
    private val downloadVideoUseCase: DownloadVideoUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val openFileUseCase: OpenFileUseCase,
    private val reactToMessageUseCase: ReactToMessageUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val replyMessageUseCase: ReplyMessageUseCase,
    private val pinMessageUseCase: PinMessageUseCase,
    private val observePinnedMessageUseCase: ObservePinnedMessageUseCase,
    private val unpinMessageUseCase: UnpinMessageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ConversationDetailUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ConversationDetailEvent>()
    val event = _event.asSharedFlow()

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
                .getOrElse {
                    _uiState.value = UiState.Error
                    return@launch
                }
            getConversationByIdUseCase(conversationId)
                .onSuccess { data ->
                    when (data) {
                        is ConversationDetailData.Direct -> {
                            observeDirectConversation(data, currentUser)
                        }

                        is ConversationDetailData.Group -> {
                            observeGroupConversation(data, currentUser)
                        }
                    }
                }
                .onFailure {
                    _uiState.value = UiState.Error
                }
        }
    }

    private suspend fun observeDirectConversation(
        data: ConversationDetailData.Direct,
        currentUser: User
    ) {
        combine(
            observeFriendshipUseCase(data.user.id),
            observeFriendRequestUseCase(data.user.id),
            observeMessagesByConversationIdUseCase(data.conversation.id),
            observePinnedMessageUseCase(data.conversation.id)
        ) { isFriend, isSentRequest, messages, pinnedMessages ->
            ConversationDetailUiState(
                conversationDetailData = data,
                currentUser = currentUser,
                isConnected = isFriend,
                isSentRequest = isSentRequest,
                messages = messages,
                pinnedMessages = pinnedMessages
            )
        }
            .catch {
                _uiState.value = UiState.Error
            }
            .collect { state ->
                _uiState.value = UiState.Success(state)
            }
    }

    private suspend fun observeGroupConversation(
        data: ConversationDetailData.Group,
        currentUser: User
    ) {
        combine(
//            observeFriendshipUseCase(data.group.id),
//            observeFriendRequestUseCase(data.group.id),
            observeMessagesByConversationIdUseCase(data.conversation.id),
            observePinnedMessageUseCase(data.conversation.id)
        ) { messages, pinnedMessages ->
            ConversationDetailUiState(
                conversationDetailData = data,
                currentUser = currentUser,
                isConnected = false,
                isSentRequest = false,
                messages = messages,
                pinnedMessages = pinnedMessages
            )
        }
            .catch {
                _uiState.value = UiState.Error
            }
            .collect { state ->
                _uiState.value = UiState.Success(state)
            }
    }

    fun sendFriendRequest(receiverId: String) {
        viewModelScope.launch {
            sendFriendRequestUseCase(receiverId)
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun recallFriendRequest(request: FriendRequest) {
        viewModelScope.launch {
            recallFriendRequestUseCase(request)
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun loadMoreMessage(
        conversationId: String,
        lastVisibleTimestamp: Timestamp
    ) {
        viewModelScope.launch {
            val messages = loadMoreMessagesUseCase(conversationId, lastVisibleTimestamp)
        }
    }

    fun sendTextMessage(
        conversationId: String,
        text: String
    ) {
        viewModelScope.launch {
            sendTextMessageUseCase(conversationId, text)
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun sendMediaMessage(
        conversationId: String,
        fileInfos: List<FileInfo>
    ) {
        viewModelScope.launch {
            fileInfos.forEach { fileInfo ->
                when {
                    fileInfo.mimeType.startsWith("image/") -> {
                        sendImageMessageUseCase(conversationId, fileInfo)
                            .onFailure {
                                _event.emit(ConversationDetailEvent.ShowError)
                            }
                    }

                    fileInfo.mimeType.startsWith("video/") -> {
                        sendVideoMessageUseCase(conversationId, fileInfo)
                            .onFailure {
                                _event.emit(ConversationDetailEvent.ShowError)
                            }
                    }
                }
            }
        }
    }

    fun sendFileMessage(
        conversationId: String,
        fileInfos: List<FileInfo>
    ) {
        viewModelScope.launch {
            fileInfos.forEach { fileInfo ->
                sendFileMessageUseCase(conversationId, fileInfo)
                    .onFailure {
                        _event.emit(ConversationDetailEvent.ShowError)
                    }
            }
        }
    }

    fun markMessageAsRead(
        conversationId: String,
        messageId: String
    ) {
        viewModelScope.launch {
            markMessageAsReadUseCase(conversationId, messageId)
        }
    }

    fun downloadImage(
        context: Context,
        imageUrl: String,
        fileName: String,
        mimeType: String
    ) {
        viewModelScope.launch {
            downloadImageUseCase(
                context = context,
                imageUrl = imageUrl,
                fileName = fileName,
                mimeType = mimeType
            )
                .onSuccess {
                    _event.emit(ConversationDetailEvent.DownloadSuccess)
                }
                .onFailure {
                    _event.emit(ConversationDetailEvent.DownloadFailed)
                }
        }
    }

    fun downloadVideo(
        context: Context,
        videoUrl: String,
        fileName: String,
        mimeType: String
    ) {
        viewModelScope.launch {
            downloadVideoUseCase(
                context = context,
                videoUrl = videoUrl,
                fileName = fileName,
                mimeType = mimeType
            )
                .onSuccess {
                    _event.emit(ConversationDetailEvent.DownloadSuccess)
                }
                .onFailure {
                    _event.emit(ConversationDetailEvent.DownloadFailed)
                }
        }
    }

    fun downloadFile(
        context: Context,
        fileUrl: String,
        fileName: String,
        mimeType: String
    ) {
        viewModelScope.launch {
            downloadFileUseCase(
                context = context,
                fileUrl = fileUrl,
                fileName = fileName,
                mimeType = mimeType
            )
                .onSuccess {
                    _event.emit(ConversationDetailEvent.DownloadSuccess)
                }
                .onFailure {
                    _event.emit(ConversationDetailEvent.DownloadFailed)
                }
        }
    }

    fun openFile(
        context: Context,
        fileName: String,
        mimeType: String
    ) {
        viewModelScope.launch {
            openFileUseCase(
                context = context,
                fileName = fileName,
                mimeType = mimeType
            )
                .onFailure {
                    _event.emit(ConversationDetailEvent.OpenFileFailed)
                }
        }
    }

    fun reactToMessage(
        conversationId: String,
        messageId: String,
        emoji: String
    ) {
        viewModelScope.launch {
            reactToMessageUseCase(
                conversationId = conversationId,
                messageId = messageId,
                emoji = emoji
            )
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun removeReaction(
        conversationId: String,
        messageId: String
    ) {
        viewModelScope.launch {
            removeReactionUseCase(
                conversationId = conversationId,
                messageId = messageId
            )
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun replyMessage(
        conversationId: String,
        text: String,
        replyPreview: ReplyPreview
    ) {
        viewModelScope.launch {
            replyMessageUseCase(
                conversationId = conversationId,
                text = text,
                replyPreview = replyPreview
            )
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun pinMessage(
        conversationId: String,
        messageId: String,
        isMaximum: Boolean
    ) {
        viewModelScope.launch {
            if(isMaximum) {
                _event.emit(ConversationDetailEvent.PinnedMessageMaximum)
                return@launch
            }

            pinMessageUseCase(conversationId, messageId)
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }

    fun unpinMessage(
        conversationId: String,
        messageId: String
    ) {
        viewModelScope.launch {
            unpinMessageUseCase(conversationId, messageId)
                .onFailure {
                    _event.emit(ConversationDetailEvent.ShowError)
                }
        }
    }
}
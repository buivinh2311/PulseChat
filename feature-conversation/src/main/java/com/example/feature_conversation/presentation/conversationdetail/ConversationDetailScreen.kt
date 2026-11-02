package com.example.feature_conversation.presentation.conversationdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData.newPlainText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_model.Message
import com.example.core_model.ReplyPreview
import com.example.core_model.composite.MediaPreview
import com.example.core_model.enum.MessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.model.ConversationDetailAction
import com.example.feature_conversation.presentation.conversationdetail.component.ConversationDetailBottomBar
import com.example.feature_conversation.presentation.conversationdetail.component.ConversationDetailContent
import com.example.feature_conversation.presentation.conversationdetail.component.ConversationDetailTopBar
import com.example.feature_conversation.presentation.conversationdetail.component.ImagePreviewDialog
import com.example.feature_conversation.presentation.conversationdetail.component.MessagePopup
import com.example.feature_conversation.presentation.conversationdetail.component.PinnedMessagesPreview
import com.example.feature_conversation.presentation.conversationdetail.component.UnpinMessageDialog
import com.example.feature_conversation.presentation.conversationdetail.component.VideoPreviewDialog
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ConversationDetailScreen(
    conversationId: String,
    darkTheme: Boolean,
    onBack: () -> Unit,
    onOption: (String) -> Unit,
    onOpenCamera: () -> Unit,
    onForward: (String, String) -> Unit
) {
    val conversationDetailViewModel: ConversationDetailViewModel = hiltViewModel()
    val uiState by conversationDetailViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(conversationId) {
        conversationDetailViewModel.loadConversation(conversationId)
    }

    var expanded by remember { mutableStateOf(false) }
    val view = LocalView.current
    val window = (view.context as Activity).window
    val systemBarColor = if (expanded) {
        MaterialTheme.colorScheme.scrim
    } else {
        MaterialTheme.colorScheme.surface
    }
    SideEffect {
        window.statusBarColor = systemBarColor.toArgb()
        window.navigationBarColor = systemBarColor.toArgb()

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !expanded && !darkTheme
            isAppearanceLightNavigationBars = !expanded && !darkTheme
        }
    }

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        conversationDetailViewModel.event.collect { event ->
            when (event) {
                ConversationDetailEvent.ShowError -> {
                    showToast(
                        context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }

                ConversationDetailEvent.DownloadSuccess -> {
                    showToast(
                        context,
                        message = context.getString(R.string.download_success)
                    )
                }

                ConversationDetailEvent.DownloadFailed -> {
                    showToast(
                        context,
                        message = context.getString(R.string.download_failed)
                    )
                }

                ConversationDetailEvent.OpenFileFailed -> {
                    showToast(
                        context,
                        message = context.getString(R.string.open_file_failed)
                    )
                }

                ConversationDetailEvent.PinnedMessageMaximum -> {
                    expanded = true
                    showToast(
                        context,
                        message = context.getString(R.string.text_pinned_message_maximum)
                    )
                }
            }
        }
    }

    var mediaPreview: MediaPreview? by remember {
        mutableStateOf(null)
    }

    var replyPreview: ReplyPreview? by remember {
        mutableStateOf(null)
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(replyPreview) {
        if (replyPreview != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    var scrollRequest by remember {
        mutableIntStateOf(0)
    }
    var targetMessageId by remember {
        mutableStateOf<String?>(null)
    }

    fun onPinnedMessageClick(messageId: String) {
        targetMessageId = messageId
        scrollRequest++
    }

    var unpinMessage by remember {
        mutableStateOf<Message?>(null)
    }

    var selectedMessage by remember {
        mutableStateOf<Message?>(null)
    }

    val onAction: (ConversationDetailAction) -> Unit = { action ->
        when (action) {
            // Media
            is ConversationDetailAction.DownloadImage -> {
                val imageMessage = action.imageMessage
                conversationDetailViewModel.downloadImage(
                    context = context,
                    imageUrl = imageMessage.fileUrl,
                    fileName = imageMessage.fileName,
                    mimeType = imageMessage.mimeType
                )
            }

            is ConversationDetailAction.DownloadVideo -> {
                val videoMessage = action.videoMessage
                conversationDetailViewModel.downloadVideo(
                    context = context,
                    videoUrl = videoMessage.fileUrl,
                    fileName = videoMessage.fileName,
                    mimeType = videoMessage.mimeType
                )
            }

            is ConversationDetailAction.FileSelected -> {
                conversationDetailViewModel.sendFileMessage(
                    conversationId,
                    action.fileInfos
                )
            }

            is ConversationDetailAction.MediaSelected -> {
                conversationDetailViewModel.sendMediaMessage(
                    conversationId,
                    action.fileInfos
                )
            }

            is ConversationDetailAction.OpenMedia -> {
                mediaPreview = action.mediaPreview
            }

            is ConversationDetailAction.DownloadFile -> {
                val fileMessage = action.fileMessage
                conversationDetailViewModel.downloadFile(
                    context = context,
                    fileUrl = fileMessage.fileUrl,
                    fileName = fileMessage.fileName,
                    mimeType = fileMessage.mimeType
                )
            }

            is ConversationDetailAction.OpenFile -> {
                val fileMessage = action.fileMessage
                conversationDetailViewModel.openFile(
                    context = context,
                    fileName = fileMessage.fileName,
                    mimeType = fileMessage.mimeType
                )
            }

            // Message
            is ConversationDetailAction.SendMessage -> {
                conversationDetailViewModel.sendTextMessage(
                    conversationId,
                    action.message
                )
            }

            is ConversationDetailAction.SendReplyMessage -> {
                conversationDetailViewModel.replyMessage(
                    conversationId = conversationId,
                    text = action.text,
                    replyPreview = action.replyPreview
                )
                replyPreview = null
            }

            is ConversationDetailAction.ReactToMessage -> {
                conversationDetailViewModel.reactToMessage(
                    conversationId = conversationId,
                    messageId = action.messageId,
                    emoji = action.emoji
                )
            }

            is ConversationDetailAction.RemoveReaction -> {
                conversationDetailViewModel.removeReaction(
                    conversationId = conversationId,
                    messageId = action.messageId
                )
            }

            is ConversationDetailAction.Reply -> {
                val message = action.message
                replyPreview = ReplyPreview(
                    messageId = message.id,
                    senderId = message.senderId,
                    senderName = action.senderName,
                    type = when (message) {
                        is Message.Text -> MessageType.TEXT
                        is Message.File -> MessageType.FILE
                        is Message.Video -> MessageType.VIDEO
                        is Message.Image -> MessageType.IMAGE
                        is Message.System -> MessageType.SYSTEM
                    },
                    mimeType = when (message) {
                        is Message.Text -> null
                        is Message.File -> message.mimeType
                        is Message.Image -> message.mimeType
                        is Message.Video -> message.mimeType
                        is Message.System -> null
                    },
                    contentPreview = when (message) {
                        is Message.Text -> message.text
                        is Message.File -> message.fileName
                        is Message.Video -> message.thumbnailUrl
                        is Message.Image -> message.fileUrl
                        is Message.System -> message.senderId
                    }
                )
            }

            is ConversationDetailAction.Forward -> {
                onForward(conversationId, action.messageId)
            }

            is ConversationDetailAction.CloseReplyPreview -> {
                replyPreview = null
            }

            is ConversationDetailAction.Copy -> {
                coroutineScope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            newPlainText("message", action.text)
                        )
                    )
                }
            }

            is ConversationDetailAction.Pin -> {
                conversationDetailViewModel.pinMessage(
                    conversationId = conversationId,
                    messageId = action.messageId,
                    isMaximum = action.pinnedMessageCount >= 5
                )
            }

            is ConversationDetailAction.Unpin -> {
                conversationDetailViewModel.unpinMessage(
                    conversationId = conversationId,
                    messageId = action.messageId
                )
            }

            // Request
            is ConversationDetailAction.SendFriendRequest -> {

            }

            is ConversationDetailAction.RecallFriendRequest -> {

            }

            is ConversationDetailAction.StickerSelected -> {

            }
        }
    }

    when (val state = uiState) {
        UiState.Loading -> {
            LoadingScreen()
        }

        UiState.Error -> {
            ErrorScreen(title = "error")
        }

        UiState.Empty -> {

        }

        is UiState.Success -> {
            val conversationDetailUiState = state.data
            val messages = conversationDetailUiState.messages
            val conversationDetailData = conversationDetailUiState.conversationDetailData
            val users = when (conversationDetailData) {
                is ConversationDetailData.Direct
                    -> listOf(conversationDetailData.user, conversationDetailUiState.currentUser)

                is ConversationDetailData.Group -> conversationDetailData.users.values.toList()
            }
            val pinnedMessages = conversationDetailUiState.pinnedMessages
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    ConversationDetailTopBar(
                        conversationDetailUiState = conversationDetailUiState,
                        onBack = onBack,
                        onOption = {
                            onOption(conversationId)
                        }
                    )
                },
                bottomBar = {
                    ConversationDetailBottomBar(
                        context = context,
                        replyPreview = replyPreview,
                        focusRequester = focusRequester,
                        onAction = onAction,
                        onOpenCamera = onOpenCamera
                    )
                }
            ) { innerPadding ->
                ConversationDetailContent(
                    data = conversationDetailData,
                    currentUser = conversationDetailUiState.currentUser,
                    users = users,
                    isConnected = false,
                    isSentRequest = true,
                    messages = messages,
                    targetMessageId = targetMessageId,
                    scrollRequest = scrollRequest,
                    innerPadding = innerPadding,
                    onLongClick = { message ->
                        selectedMessage = message
                    },
                    onAction = onAction
                )
            }

            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.8f))
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            expanded = false
                        }
                )
            }

            if (pinnedMessages.isNotEmpty()) {
                PinnedMessagesPreview(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(
                            top = AppDimens.Layout.TopBarHeight + AppDimens.Space.Sm
                        ),
                    expanded = expanded,
                    users = users,
                    pinnedMessages = pinnedMessages,
                    onExpandedChange = { expanded = it },
                    onMessageClick = { messageId ->
                        onPinnedMessageClick(messageId)
                    },
                    onRemove = { message ->
                        unpinMessage = message
                    }
                )
            }

            unpinMessage?.let { message ->
                UnpinMessageDialog(
                    message = message,
                    onDismiss = { unpinMessage = null },
                    onConfirm = {
                        if(pinnedMessages.size == 1) {
                            expanded = false
                        }
                        conversationDetailViewModel.unpinMessage(
                            conversationId = conversationId,
                            messageId = message.id
                        )
                    }
                )
            }

            selectedMessage?.let { message ->
                val sender = users.find { it.id == message.senderId }
                MessagePopup(
                    message = message,
                    pinnedMessageCount = pinnedMessages.size,
                    sender = sender,
                    isMine = message.senderId == conversationDetailUiState.currentUser.id,
                    onDismiss = {
                        selectedMessage = null
                    },
                    onAction = onAction
                )
            }

            mediaPreview?.let { preview ->
                val message = messages.find { it.id == preview.messageId }
                when (message) {
                    is Message.Image -> {
                        ImagePreviewDialog(
                            imageMessage = message,
                            imagePreview = preview,
                            onDismiss = {
                                mediaPreview = null
                            },
                            onAction = onAction
                        )
                    }

                    is Message.Video -> {
                        VideoPreviewDialog(
                            videoMessage = message,
                            videoPreview = preview,
                            onDismiss = {
                                mediaPreview = null
                            },
                            onAction = onAction
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
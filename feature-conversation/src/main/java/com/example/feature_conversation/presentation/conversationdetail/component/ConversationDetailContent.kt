package com.example.feature_conversation.presentation.conversationdetail.component

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_model.composite.MediaPreview
import com.example.core_model.enum.SystemMessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_utils.getReactionCount
import com.example.feature_conversation.domain.model.ConversationDetailAction
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun ConversationDetailContent(
    data: ConversationDetailData,
    currentUser: User,
    users: List<User>,
    isConnected: Boolean,
    isSentRequest: Boolean,
    messages: List<Message>,
    targetMessageId: String?,
    scrollRequest: Int,
    innerPadding: PaddingValues,
    onLongClick: (Message) -> Unit,
    onAction: (ConversationDetailAction) -> Unit
) {
    var reactionMessage by remember {
        mutableStateOf<Message?>(null)
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var highlightedMessageId by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(scrollRequest) {
        val targetId = targetMessageId ?: return@LaunchedEffect
        val index = messages.indexOfFirst { it.id == targetId }

        if (index != -1) {
            listState.animateScrollToItem(
                index = index + 1,
                scrollOffset = -600
            )
            highlightedMessageId = targetId
        }
    }

    LaunchedEffect(highlightedMessageId) {
        if (highlightedMessageId == null) return@LaunchedEffect
        snapshotFlow {
            listState.isScrollInProgress
        }.first { isScrolling ->
            isScrolling
        }
        highlightedMessageId = null
    }

    fun scrollToMessage(messageId: String) {
        val index = messages.indexOfFirst {
            it.id == messageId
        }

        coroutineScope.launch {
            listState.animateScrollToItem(
                index = index + 1,
                scrollOffset = -600
            )
            highlightedMessageId = messageId
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = AppDimens.Space.Lg)
        ) {
            item {
                Spacer(modifier = Modifier.height(AppDimens.Space.Xxl + AppDimens.Space.Xl))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = when (data) {
                            is ConversationDetailData.Direct -> data.user.avatarUrl
                            is ConversationDetailData.Group -> data.group.avatarUrl
                        },
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.account),
                        error = painterResource(R.drawable.account),
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .aspectRatio(1f)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                if (isConnected) {
                    Spacer(modifier = Modifier.height(AppDimens.Space.Xl))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
//                    AppButton(
//                        modifier = Modifier.fillMaxWidth(0.35f),
//                        label = if(!isSentRequest)
//                            stringResource(R.string.add_friend)
//                        else
//                            stringResource(R.string.recall),
//                        color = if(!isSentRequest)
//                            MaterialTheme.colorScheme.primary
//                        else
//                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
//                        onClick = {
//                            if(isSentRequest) {
//                                onSendFriendRequest(user.id)
//                            } else {
//                                onRecallFriendRequest(user.id)
//                            }
//                        }
//                    )
                    }
                }
                Spacer(modifier = Modifier.height(AppDimens.Space.Xxl))
            }

            items(
                count = messages.size,
                key = { index -> messages[index].id }
            ) { index ->
                val message = messages[index]
                val prevMessage = if (index != 0) messages[index - 1] else null
                val firstInGroup = prevMessage == null || (prevMessage.senderId != message.senderId)
                val isMine = message.senderId == currentUser.id
                val sender = users.find { it.id == message.senderId }
                when (message) {
                    is Message.Text -> {
                        TextMessageItem(
                            sender = sender,
                            textMessage = message,
                            isMine = isMine,
                            isHighlighted = message.id == highlightedMessageId,
                            firstInGroup = firstInGroup,
                            onLongClick = {
                                onLongClick(message)
                            },
                            onReactionClick = {
                                reactionMessage = message
                            }
                        )
                    }

                    is Message.Image -> {
                        ImageMessageItem(
                            sender = sender,
                            imageMessage = message,
                            isMine = isMine,
                            isHighlighted = message.id == highlightedMessageId,
                            firstInGroup = firstInGroup,
                            onClick = {
                                val mediaPreview = MediaPreview(
                                    messageId = message.id,
                                    currentUser = currentUser,
                                    sender = sender,
                                    users = users
                                )
                                onAction(
                                    ConversationDetailAction.OpenMedia(mediaPreview)
                                )
                            },
                            onLongClick = {
                                onLongClick(message)
                            },
                            onReactionClick = {
                                reactionMessage = message
                            }
                        )
                    }

                    is Message.Video -> {
                        VideoMessageItem(
                            sender = sender,
                            videoMessage = message,
                            isMine = isMine,
                            isHighlighted = message.id == highlightedMessageId,
                            firstInGroup = firstInGroup,
                            onClick = {
                                val mediaPreview = MediaPreview(
                                    messageId = message.id,
                                    currentUser = currentUser,
                                    sender = sender,
                                    users = users
                                )
                                onAction(
                                    ConversationDetailAction.OpenMedia(mediaPreview)
                                )
                            },
                            onLongClick = {
                                onLongClick(message)
                            },
                            onReactionClick = {
                                reactionMessage = message
                            }
                        )
                    }

                    is Message.File -> {
                        FileMessageItem(
                            sender = sender,
                            fileMessage = message,
                            isMine = true,
                            isHighlighted = message.id == highlightedMessageId,
                            firstInGroup = firstInGroup,
                            onClick = {
                                onAction(
                                    ConversationDetailAction.OpenFile(message)
                                )
                            },
                            onLongClick = {
                                onLongClick(message)
                            },
                            onReactionClick = {
                                reactionMessage = message
                            }
                        )
                    }

                    is Message.System -> {
                        SystemMessageItem(
                            isMine = message.senderId == currentUser.id,
                            actorUserName = sender?.fullName
                                ?: stringResource(R.string.text_unknown_user),
                            systemMessage = message,
                            onView = when (message.systemMessageType) {
                                SystemMessageType.PIN_MESSAGE,
                                SystemMessageType.UNPIN_MESSAGE -> {
                                    message.targetMessageId?.let { targetId ->
                                        { scrollToMessage(targetId) }
                                    }
                                }

                                else -> null
                            }

                        )
                    }
                }
            }
        }

        reactionMessage?.let { message ->
            val reactionUsers = message.reactions.mapNotNull { (userId, emoji) ->
                users.find { it.id == userId }?.let { user ->
                    user to emoji
                }
            }
            ReactionBottomSheet(
                currentUserId = currentUser.id,
                reactionUsers = reactionUsers,
                onDismissRequest = {
                    reactionMessage = null
                },
                onRemoveReaction = {
                    onAction(
                        ConversationDetailAction.RemoveReaction(message.id)
                    )
                }
            )
        }
    }
}
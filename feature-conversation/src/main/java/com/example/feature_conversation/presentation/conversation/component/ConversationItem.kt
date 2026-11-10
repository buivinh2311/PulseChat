package com.example.feature_conversation.presentation.conversation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_model.Conversation
import com.example.core_model.Message
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_model.composite.ConversationWithInfo
import com.example.core_model.enum.ConversationType
import com.example.core_model.enum.MessageType
import com.example.core_model.enum.SystemMessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_utils.toRelativeString

@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    currentUserId: String,
    conversationWithInfo: ConversationWithInfo,
    onClick: (String) -> Unit
) {
    val conversation = conversationWithInfo.conversation
    val displayInfo = conversationWithInfo.displayInfo
    Surface(
        modifier = modifier
            .height(AppDimens.Layout.ChatItemHeight)
            .padding(horizontal = AppDimens.Space.Md)
            .shadow(
                elevation = AppDimens.Space.Es,
                shape = RoundedCornerShape(AppDimens.Space.Lg)
            ),
        onClick = {
            onClick(conversation.id)
        },
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = AppDimens.Space.Md
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = when (displayInfo) {
                        is ConversationDisplayInfo.Direct -> displayInfo.otherUser.avatarUrl
                        is ConversationDisplayInfo.Group -> displayInfo.group.avatarUrl
                    },
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.account),
                    error = painterResource(R.drawable.account),
                    modifier = Modifier
                        .size(AppDimens.ImageSize.Sm)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )

                if (conversationWithInfo.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(AppDimens.Icon.Es)
                            .clip(shape = CircleShape)
                            .background(color = Color(0xFF31A24C))
                            .border(
                                width = AppDimens.Border.Thick - AppDimens.Border.Normal,
                                color = MaterialTheme.colorScheme.background,
                                shape = CircleShape
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.Space.Md))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (displayInfo) {
                            is ConversationDisplayInfo.Direct ->
                                displayInfo.otherUser.fullName

                            is ConversationDisplayInfo.Group -> displayInfo.group.name
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                    conversation.pinnedAt?.let {
                        Icon(
                            painter = AppIcons.Pin,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(AppDimens.Icon.Es)
                        )
                    }

                    conversation.lastMessageTime?.let {
                        Spacer(modifier = Modifier.width(AppDimens.Space.Xxs))
                        Text(
                            text = it.toRelativeString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                conversation.lastMessageType?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when(conversation.lastMessageType) {
                                MessageType.SYSTEM -> {
                                    if (currentUserId == conversation.lastMessageActorId) {
                                        stringResource(
                                            R.string.text_my_last_system_message,
                                            getLastMessagePreview(conversation)
                                        )
                                    } else {
                                        "${conversation.lastMessageActorName} " +
                                                getLastMessagePreview(conversation)
                                    }
                                }

                                else -> {
                                    if (currentUserId == conversation.lastMessageActorId) {
                                        stringResource(
                                            R.string.text_my_last_message,
                                            getLastMessagePreview(conversation)
                                        )
                                    } else {
                                        "${conversation.lastMessageActorName}: " +
                                                getLastMessagePreview(conversation)
                                    }
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(AppDimens.Space.Lg))
                        Text(
                            text = "3",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimens.Radius.Lg))
                                .background(MaterialTheme.colorScheme.error)
                                .padding(
                                    vertical = AppDimens.Space.Xs,
                                    horizontal = AppDimens.Space.Sm
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getLastMessagePreview(
    conversation: Conversation
): String {
    return when (conversation.lastMessageType) {
        MessageType.TEXT, MessageType.FILE -> conversation.lastMessageText.orEmpty()
        MessageType.IMAGE -> stringResource(R.string.text_image)
        MessageType.VIDEO -> stringResource(R.string.text_video)
        MessageType.SYSTEM -> {
            when (conversation.systemMessageType) {
                SystemMessageType.CREATE_GROUP
                    -> stringResource(R.string.text_system_create_group)

                SystemMessageType.CHANGE_GROUP_NAME
                    -> stringResource(
                    R.string.text_system_change_group_name,
                    conversation.lastMessageText.orEmpty()
                )

                SystemMessageType.ADD_GROUP_MEMBER
                    -> stringResource(
                    R.string.text_system_add_group_member,
                    conversation.lastMessageText.orEmpty()
                )

                SystemMessageType.PIN_MESSAGE
                    -> stringResource(R.string.text_system_pin_message)

                SystemMessageType.UNPIN_MESSAGE
                    -> stringResource(R.string.text_system_unpin_message)

                else -> ""
            }
        }

        else -> ""
    }
}
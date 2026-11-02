package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_model.enum.MessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_utils.getReactionCount

@Composable
fun TextMessageItem(
    modifier: Modifier = Modifier,
    sender: User?,
    textMessage: Message.Text,
    isMine: Boolean,
    isHighlighted: Boolean,
    firstInGroup: Boolean,
    onLongClick: () -> Unit,
    onReactionClick: () -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    val reactionCounts = textMessage.getReactionCount()
    val showAvatar = firstInGroup && !isMine
    Row(
        modifier = Modifier
            .padding(horizontal = AppDimens.Space.Md)
            .then(
                if (firstInGroup) {
                    Modifier.padding(
                        top = AppDimens.Space.Sm,
                        bottom = AppDimens.Space.Xxs
                    )
                } else {
                    Modifier.padding(
                        start = AppDimens.Space.Xl + AppDimens.Space.Sm,
                        top = AppDimens.Space.Xxs,
                        bottom = AppDimens.Space.Xxs
                    )
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(
            AppDimens.Space.Xs + AppDimens.Space.Xxs
        )
    ) {
        if (showAvatar) {
            AsyncImage(
                model = sender?.avatarUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(AppDimens.ImageSize.Xxs)
                    .clip(CircleShape)
                    .border(
                        width = AppDimens.Border.Thin,
                        color = MaterialTheme.colorScheme
                            .outlineVariant.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
                    .clickable {

                    }
            )
        }

        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            if (isMine) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .fillMaxWidth(0.25f)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            onDismiss?.invoke()
                        }
                )
            }

            if (!isMine) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .fillMaxWidth(if (showAvatar) 0.15f else 0.25f)
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            onDismiss?.invoke()
                        }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(
                        if (showAvatar) 0.85f
                        else 0.75f
                    )
                    .align(
                        alignment = if (isMine) Alignment.CenterEnd
                        else Alignment.CenterStart
                    )
            ) {
                if (isMine) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismiss?.let {
                                    onDismiss()
                                }
                            }
                    )
                }

                Box {
                    Column(
                        modifier = Modifier
                            .align(
                                if (isMine) Alignment.CenterEnd
                                else Alignment.CenterStart
                            )
                            .widthIn(min = AppDimens.Layout.MinTextMessageWidth)
                            .clip(
                                RoundedCornerShape(
                                    AppDimens.Radius.Sm + AppDimens.Radius.Xxs
                                )
                            )
                            .then(
                                if (isMine) {
                                    Modifier.background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF0073DF),
                                                MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    )
                                } else {
                                    Modifier.background(MaterialTheme.colorScheme.surface)
                                }
                            )
                            .border(
                                width = if (isHighlighted) {
                                    AppDimens.Border.Thick - AppDimens.Border.Normal
                                } else {
                                    AppDimens.Border.Thin
                                },
                                color = if (isHighlighted) {
                                    Color(0xFFE9A23B)
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                                },
                                shape = RoundedCornerShape(
                                    AppDimens.Radius.Sm + AppDimens.Radius.Xxs
                                )
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        onLongClick()
                                    }
                                )
                            }
                            .padding(
                                vertical = AppDimens.Space.Sm + AppDimens.Space.Xxs,
                                horizontal = AppDimens.Space.Md
                            ),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm)
                    ) {
                        textMessage.replyPreview?.let { replyPreview ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm)
                            ) {
                                VerticalDivider(
                                    thickness = AppDimens.Divider.Thick,
                                    color = if (isMine) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .height(
                                            AppDimens.Space.Xl + AppDimens.Space.Md
                                        )
                                        .clip(RoundedCornerShape(AppDimens.Radius.Xs))
                                )

                                if (
                                    replyPreview.type == MessageType.IMAGE ||
                                    replyPreview.type == MessageType.VIDEO
                                ) {
                                    AsyncImage(
                                        model = replyPreview.contentPreview,
                                        contentDescription = null,
                                        placeholder = AppIcons.Image,
                                        error = AppIcons.Image,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(AppDimens.ImageSize.Xs)
                                            .clip(RoundedCornerShape(AppDimens.Space.Sm))
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .padding(end = AppDimens.Space.Sm),
                                    verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xxs)
                                ) {
                                    Text(
                                        text = replyPreview.senderName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isMine) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = when (replyPreview.type) {
                                            MessageType.TEXT -> replyPreview.contentPreview
                                            MessageType.FILE -> stringResource(
                                                R.string.text_file,
                                                replyPreview.contentPreview
                                            )

                                            MessageType.IMAGE
                                                -> stringResource(R.string.text_image)

                                            MessageType.VIDEO
                                                -> stringResource(R.string.text_video)

                                            MessageType.SYSTEM
                                                -> stringResource(R.string.text_system)
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isMine) MaterialTheme.colorScheme.onPrimary.copy(
                                            0.7f
                                        )
                                        else MaterialTheme.colorScheme.outlineVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Text(
                            text = textMessage.text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .widthIn(min = AppDimens.Layout.MinTextMessageWidth),
                            color = if (isMine) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                    if (reactionCounts.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(
                                    x = -AppDimens.Space.Sm,
                                    y = AppDimens.Space.Lg
                                )
                                .clip(RoundedCornerShape(AppDimens.Radius.Xl))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    width = AppDimens.Border.Thin,
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(AppDimens.Radius.Xl)
                                )
                                .clickable {
                                    onReactionClick()
                                }
                                .padding(horizontal = AppDimens.Space.Xs + AppDimens.Space.Xxs),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xxs)
                        ) {
                            var value = 0
                            reactionCounts.forEach { (emoji, count) ->
                                value += count
                                Text(
                                    text = emoji,
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            if (value != 0) {
                                Text(
                                    text = value.toString(),
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }

                if (!isMine) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismiss?.let {
                                    onDismiss()
                                }
                            }
                    )
                }
            }
        }
    }

    if (reactionCounts.isNotEmpty()) {
        Spacer(
            modifier = Modifier.height(AppDimens.Space.Lg)
        )
    }
}
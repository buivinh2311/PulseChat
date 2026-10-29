package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_utils.FileUtil
import com.example.core_utils.getReactionCount

@Composable
fun FileMessageItem(
    sender: User?,
    fileMessage: Message.File,
    isMine: Boolean,
    isHighlighted: Boolean,
    firstInGroup: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onReactionClick: () -> Unit,
    onDismiss: (() -> Unit)? = null
) {
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
            modifier = Modifier.fillMaxWidth()
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

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    AppDimens.Radius.Sm + AppDimens.Radius.Xxs
                                )
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
                            .align(
                                alignment = if (isMine) Alignment.CenterEnd
                                else Alignment.CenterStart
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
                            .combinedClickable(
                                onClick = onClick,
                                onLongClick = onLongClick
                            )
                            .padding(
                                vertical = AppDimens.Space.Md,
                                horizontal = AppDimens.Space.Sm
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(getFileImage(fileMessage.mimeType)),
                            contentDescription = "file image",
                            modifier = Modifier.size(AppDimens.ImageSize.Xs)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.Space.Xs))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = fileMessage.fileName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))

                            Text(
                                text = FileUtil.formatFileSize(fileMessage.fileSize),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                            }
                        }
                    }

                    if (fileMessage.reactions.isNotEmpty()) {
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
                            fileMessage.getReactionCount().forEach { (emoji, count) ->
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

    if (fileMessage.reactions.isNotEmpty()) {
        Spacer(
            modifier = Modifier.height(AppDimens.Space.Lg)
        )
    }
}

@DrawableRes
fun getFileImage(mimeType: String): Int {
    return when {
        mimeType == "application/pdf" -> R.drawable.pdf

        mimeType == "application/zip" ||
                mimeType == "application/x-rar-compressed" ||
                mimeType == "application/x-7z-compressed" ->
            R.drawable.zip

        mimeType == "text/plain" -> R.drawable.file

        mimeType == "application/msword" ||
                mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
            R.drawable.doc

        mimeType == "application/vnd.ms-excel" ||
                mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ->
            R.drawable.sheets

        mimeType == "application/vnd.ms-powerpoint" ||
                mimeType == "application/vnd.openxmlformats-officedocument.presentationml.presentation" ->
            R.drawable.ppt

        else -> R.drawable.file
    }
}
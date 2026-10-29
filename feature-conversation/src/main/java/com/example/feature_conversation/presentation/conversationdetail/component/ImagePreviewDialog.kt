package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.composite.MediaPreview
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_utils.SystemBarsUtil
import com.example.core_utils.getReactionCount
import com.example.core_utils.toRelativeString
import com.example.feature_conversation.domain.model.ConversationDetailAction

@Composable
fun ImagePreviewDialog(
    imageMessage: Message.Image,
    imagePreview: MediaPreview,
    onDismiss: () -> Unit,
    onAction: (ConversationDetailAction) -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isVisible by remember { mutableStateOf(false) }
    val emojis = listOf("❤️", "👍", "😂", "😮", "😢", "😡")

    var showReaction by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        DisposableEffect(window) {
            if (window != null) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                SystemBarsUtil.hideSystemBars(window)
            }

            onDispose {
                if (window != null) {
                    SystemBarsUtil.showSystemBars(window)
                }
            }
        }
        val sender = imagePreview.sender

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = imageMessage.fileUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                            scale = newScale
                            if (newScale > 1f) {
                                val maxX = size.width * (newScale - 1f) / 2f
                                val maxY = size.height * (newScale - 1f) / 2f
                                offset = Offset(
                                    x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                                )
                            } else {
                                offset = Offset.Zero
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                isVisible = !isVisible
                                window?.let {
                                    if(isVisible) {
                                        SystemBarsUtil.showSystemBars(it)
                                    } else {
                                        SystemBarsUtil.hideSystemBars(it)
                                    }
                                }
                            },
                            onDoubleTap = {
                                if (scale > 1f) {
                                    scale = 1f
                                    offset = Offset.Zero
                                } else {
                                    scale = 2.5f
                                }
                            }
                        )
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(
                    animationSpec = tween(100)
                ) + slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(300)
                ),
                exit = fadeOut(
                    animationSpec = tween(100)
                ) + slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(200)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.TopStart)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                        .padding(
                            top = 36.dp,
                            bottom = AppDimens.Space.Sm,
                            start = AppDimens.Space.Xs,
                            end = AppDimens.Space.Xs
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            painter = AppIcons.Back,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(AppDimens.Icon.Md)
                        )
                    }
                    Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                    AsyncImage(
                        model = sender?.avatarUrl,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.account),
                        error = painterResource(R.drawable.account),
                        modifier = Modifier
                            .size(AppDimens.ImageSize.Sm)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = sender?.fullName
                                ?: stringResource(R.string.text_unknown_user),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = imageMessage.createdAt.toRelativeString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            onAction(
                                ConversationDetailAction.DownloadImage(imageMessage)
                            )
                        }
                    ) {
                        Icon(
                            painter = AppIcons.Download,
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(AppDimens.Icon.Md)
                        )
                    }
                    Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = AppIcons.More,
                            contentDescription = "More",
                            tint = Color.White,
                            modifier = Modifier.size(AppDimens.Icon.Md)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(
                    animationSpec = tween(100)
                ) + slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ),
                exit = fadeOut(
                    animationSpec = tween(100)
                ) + slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(color = Color.Black.copy(alpha = 0.7f))
                    .navigationBarsPadding()
                    .padding(AppDimens.Space.Md)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(imageMessage.reactions.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    showReaction = true
                                },
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                        ) {
                            imageMessage.getReactionCount().forEach { (emoji, count) ->
                                Text(
                                    text = "$emoji $count",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                        HorizontalDivider(
                            thickness = AppDimens.Divider.Normal,
                            color = Color.White.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(AppDimens.Space.Md))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
                    ) {
                        emojis.forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = Color.White.copy(alpha = 0.1f))
                                    .clickable {
                                        onAction(
                                            ConversationDetailAction.ReactToMessage(
                                                messageId = imageMessage.id,
                                                emoji = emoji
                                            )
                                        )
                                    }
                                    .border(
                                        width = AppDimens.Border.Thin,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = CircleShape
                                    )
                                    .padding(AppDimens.Space.Xs),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if(showReaction) {
        val reactionUsers = imageMessage.reactions.mapNotNull { (userId, emoji) ->
            imagePreview.users.find { it.id == userId }?.let { user ->
                user to emoji
            }
        }
        ReactionBottomSheet(
            currentUserId = imagePreview.currentUser.id,
            reactionUsers = reactionUsers,
            onDismissRequest = {
                showReaction = false
            },
            onRemoveReaction = {
                onAction(
                    ConversationDetailAction.RemoveReaction(imageMessage.id)
                )
            }
        )
    }
}
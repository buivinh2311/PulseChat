package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.annotation.OptIn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
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
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPreviewDialog (
    videoMessage: Message.Video,
    videoPreview: MediaPreview,
    onDismiss: () -> Unit,
    onAction: (ConversationDetailAction) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableLongStateOf(0L) }
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
        val sender = videoPreview.sender
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        val context = LocalContext.current
        val exoPlayer = remember(videoMessage.fileUrl) {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    setMediaItem(MediaItem.fromUri(videoMessage.fileUrl))
                    prepare()
                    playWhenReady = true
                }
        }

        LaunchedEffect(exoPlayer) {
            while (true) {
                if (!isSeeking) {
                    currentPosition = exoPlayer.currentPosition
                }
                duration = exoPlayer.duration.coerceAtLeast(0L)
                delay(500)
            }
        }

        var isPlaying by remember {
            mutableStateOf(exoPlayer.isPlaying)
        }

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

        DisposableEffect(exoPlayer) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                    isPlaying = isPlayingNow
                }
            }
            exoPlayer.addListener(listener)
            onDispose {
                exoPlayer.removeListener(listener)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                isVisible = !isVisible
                                window?.let {
                                    if (isVisible) {
                                        SystemBarsUtil.showSystemBars(it)
                                    } else {
                                        SystemBarsUtil.hideSystemBars(it)
                                    }
                                }
                            }
                        )
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
                        Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))

                        Text(
                            text = videoMessage.createdAt.toRelativeString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            onAction(
                                ConversationDetailAction.DownloadVideo(videoMessage)
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
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppDimens.Space.Sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDuration(
                                if (isSeeking) seekPosition else currentPosition
                            ),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                        Slider(
                            value = if (isSeeking) {
                                seekPosition.toFloat()
                            } else {
                                currentPosition.toFloat()
                            },
                            onValueChange = { value ->
                                isSeeking = true
                                seekPosition = value.toLong()
                            },
                            onValueChangeFinished = {
                                exoPlayer.seekTo(seekPosition)
                                currentPosition = seekPosition
                                isSeeking = false
                            },
                            valueRange = 0f..duration.coerceAtLeast(1L).toFloat(),
                            modifier = Modifier.weight(1f),
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color.White, CircleShape)
                                )
                            },
                            track = { sliderState ->
                                SliderDefaults.Track(
                                    sliderState = sliderState,
                                    modifier = Modifier.height(AppDimens.Space.Xs),
                                    thumbTrackGapSize = 0.dp
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                        Text(
                            text = formatDuration(duration),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                            .navigationBarsPadding()
                            .padding(AppDimens.Space.Md)
                    ) {
                        if(videoMessage.reactions.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        showReaction = true
                                    },
                                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                            ) {
                                videoMessage.getReactionCount().forEach { (emoji, count) ->
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
                                                    messageId = videoMessage.id,
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

            if(isVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(AppDimens.Ripple.Xl)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(
                            width = AppDimens.Border.Thin,
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable {
                            if (isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = if (isPlaying) AppIcons.Pause else AppIcons.Play,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(AppDimens.Icon.Xl)
                    )
                }
            }
        }
    }

    if(showReaction) {
        val reactionUsers = videoMessage.reactions.mapNotNull { (userId, emoji) ->
            videoPreview.users.find { it.id == userId }?.let { user ->
                user to emoji
            }
        }
        ReactionBottomSheet(
            currentUserId = videoPreview.currentUser.id,
            reactionUsers = reactionUsers,
            onDismissRequest = {
                showReaction = false
            },
            onRemoveReaction = {
                onAction(
                    ConversationDetailAction.RemoveReaction(videoMessage.id)
                )
            }
        )
    }
}

fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
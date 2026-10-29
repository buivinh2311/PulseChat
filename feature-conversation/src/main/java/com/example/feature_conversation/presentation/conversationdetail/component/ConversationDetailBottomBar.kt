package com.example.feature_conversation.presentation.conversationdetail.component

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_model.ReplyPreview
import com.example.core_model.enum.MessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconButton
import com.example.core_utils.ImageUtil
import com.example.feature_conversation.domain.model.ConversationDetailAction
import com.example.feature_conversation.domain.model.FileInfo

@Composable
fun ConversationDetailBottomBar(
    modifier: Modifier = Modifier,
    context: Context,
    replyPreview: ReplyPreview?,
    focusRequester: FocusRequester,
    onAction: (ConversationDetailAction) -> Unit,
    onOpenCamera: () -> Unit
) {
    var message by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        val fileInfos = uris.mapNotNull { uri ->
            val mimeType = context.contentResolver.getType(uri)
                ?: return@mapNotNull null
            FileInfo(
                file = ImageUtil.uriToFile(context, uri),
                mimeType = mimeType
            )
        }
        onAction(
            ConversationDetailAction.MediaSelected(fileInfos)
        )
    }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val fileInfos = uris.mapNotNull { uri ->
            val mimeType = context.contentResolver.getType(uri)
                ?: return@mapNotNull null
            FileInfo(
                file = ImageUtil.uriToFile(context, uri),
                mimeType = mimeType
            )
        }
        onAction(
            ConversationDetailAction.FileSelected(fileInfos)
        )
    }

    val gradientColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    val strokeWidth = AppDimens.Divider.Normal

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        replyPreview?.let {
            Row(
                modifier = modifier
                    .height(AppDimens.Layout.BottomBarHeight)
                    .padding(
                        top = AppDimens.Space.Xs + AppDimens.Space.Xxs,
                        bottom = AppDimens.Space.Xs + AppDimens.Space.Xxs,
                        start = AppDimens.Space.Lg,
                        end = AppDimens.Space.Md
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm)
            ) {
                VerticalDivider(
                    thickness = AppDimens.Divider.Thick,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxHeight()
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

                if(replyPreview.type == MessageType.FILE) {
                    Image(
                        painter = painterResource(
                            getFileImage(replyPreview.mimeType.orEmpty())
                        ),
                        contentDescription = "file image",
                        modifier = Modifier.size(AppDimens.ImageSize.Xs)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = AppDimens.Space.Sm),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                ) {
                    Text(
                        text = replyPreview.senderName,
                        style = MaterialTheme.typography.titleMedium,
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
                            MessageType.IMAGE -> stringResource(R.string.text_image)
                            MessageType.VIDEO -> stringResource(R.string.text_video)
                            MessageType.SYSTEM -> stringResource(R.string.text_system)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                AppIconButton(
                    icon = AppIcons.Close,
                    color = MaterialTheme.colorScheme.outline,
                    onClick = {
                        onAction(ConversationDetailAction.CloseReplyPreview)
                    }
                )
            }
        }

        Row(
            modifier = modifier
                .navigationBarsPadding()
                .imePadding()
                .drawBehind {
                    drawLine(
                        color = gradientColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth.toPx()
                    )
                }
                .padding(AppDimens.Space.Sm + AppDimens.Space.Xxs)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton(
                icon = AppIcons.Sticker,
                color = MaterialTheme.colorScheme.outline,
                onClick = {
                    onAction(ConversationDetailAction.StickerSelected)
                }
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Xs))

            BasicTextField(
                value = message,
                onValueChange = { message = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box {
                        if (message.isEmpty()) {
                            Text(
                                text = stringResource(R.string.hint_message),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

            if (message.isBlank()) {
                AppIconButton(
                    icon = AppIcons.File,
                    color = MaterialTheme.colorScheme.outline,
                    onClick = {
                        fileLauncher.launch(
                            arrayOf("*/*")
                        )
                    }
                )
                Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                AppIconButton(
                    icon = AppIcons.Mic,
                    color = MaterialTheme.colorScheme.outline,
                    onClick = onOpenCamera
                )
                Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                AppIconButton(
                    icon = AppIcons.Image,
                    color = MaterialTheme.colorScheme.outline,
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                AppIconButton(
                    icon = AppIcons.Camera,
                    color = MaterialTheme.colorScheme.outline,
                    onClick = onOpenCamera
                )
            } else {
                AppIconButton(
                    icon = AppIcons.Send,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if(replyPreview == null) {
                            onAction(
                                ConversationDetailAction.SendMessage(message)
                            )
                        } else {
                            onAction(
                                ConversationDetailAction.SendReplyMessage(message, replyPreview)
                            )
                        }
                        message = ""
                    }
                )
            }
        }
    }
}
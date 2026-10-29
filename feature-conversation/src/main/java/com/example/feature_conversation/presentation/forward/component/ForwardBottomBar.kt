package com.example.feature_conversation.presentation.forward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_model.composite.ConversationWithInfo
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_utils.FileUtil
import com.example.feature_conversation.presentation.conversationdetail.component.formatDuration
import com.example.feature_conversation.presentation.conversationdetail.component.getFileImage
import kotlin.text.isEmpty

@Composable
fun ForwardBottomBar(
    message: Message,
    conversationWithInfoSelected: List<ConversationWithInfo>,
    onRemoveConversationWithInfo: (ConversationWithInfo) -> Unit,
    onForward: (Message, String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }

    val gradientColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
    val strokeWidth = AppDimens.Divider.Normal
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .background(MaterialTheme.colorScheme.surface)
                .drawBehind {
                    drawLine(
                        color = gradientColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth.toPx()
                    )
                }
                .padding(vertical = AppDimens.Space.Sm)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = AppDimens.Space.Md
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
                ) {
                    items(
                        count = conversationWithInfoSelected.size,
                        key = { index -> conversationWithInfoSelected[index].conversation.id }
                    ) { index ->
                        val conversationWithInfo = conversationWithInfoSelected[index]
                        val displayInfo = conversationWithInfo.displayInfo
                        Box {
                            AsyncImage(
                                model = when (displayInfo) {
                                    is ConversationDisplayInfo.Direct
                                        -> displayInfo.otherUser.avatarUrl

                                    is ConversationDisplayInfo.Group
                                        -> displayInfo.group.avatarUrl
                                },
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.account),
                                error = painterResource(R.drawable.account),
                                modifier = Modifier
                                    .size(AppDimens.ImageSize.Sm)
                                    .aspectRatio(1f)
                                    .clip(shape = CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Icon(
                                painter = AppIcons.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .offset(
                                        x = AppDimens.Space.Xxs,
                                        y = -AppDimens.Space.Xxs
                                    )
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .size(AppDimens.Icon.Xxs)
                                    .align(Alignment.TopEnd)
                                    .border(
                                        width = AppDimens.Border.Thick,
                                        color = MaterialTheme.colorScheme.background,
                                        shape = CircleShape
                                    )
                                    .padding(AppDimens.Space.Xs)
                                    .clickable {
                                        onRemoveConversationWithInfo(conversationWithInfo)
                                    }
                            )
                        }
                    }
                }
            }

            if (conversationWithInfoSelected.isNotEmpty()) {
                HorizontalDivider(
                    thickness = AppDimens.Divider.Normal,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                    modifier = Modifier.padding(
                        vertical = AppDimens.Space.Sm,
                        horizontal = AppDimens.Space.Md
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
                    .clip(RoundedCornerShape(AppDimens.Radius.Sm))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                when (message) {
                    is Message.Text -> {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.Space.Md),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    is Message.Image -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.Space.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm)
                        ) {
                            AsyncImage(
                                model = message.fileUrl,
                                contentDescription = null,
                                placeholder = AppIcons.Image,
                                error = AppIcons.Image,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(AppDimens.ImageSize.Sm)
                                    .clip(RoundedCornerShape(AppDimens.Space.Sm))
                            )

                            Text(
                                text = stringResource(R.string.text_image_forward),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    is Message.Video -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.Space.Md),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm),
                        ) {
                            AsyncImage(
                                model = message.thumbnailUrl,
                                contentDescription = null,
                                placeholder = AppIcons.Image,
                                error = AppIcons.Image,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(AppDimens.ImageSize.Sm)
                                    .clip(RoundedCornerShape(AppDimens.Space.Sm))
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                            ) {
                                Text(
                                    text = stringResource(R.string.text_video_forward),
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = formatDuration(message.duration),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }

                    is Message.File -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.Space.Md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = getFileImage(message.mimeType),
                                contentDescription = null,
                                placeholder = AppIcons.Image,
                                error = AppIcons.Image,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(AppDimens.ImageSize.Sm)
                                    .clip(RoundedCornerShape(AppDimens.Space.Sm))
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                            ) {
                                Text(
                                    text = message.fileName,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = FileUtil.formatFileSize(message.fileSize),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }

                    is Message.System -> {

                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
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
                            if (text.isEmpty()) {
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

                Button(
                    onClick = {
                        onForward(message, text)
                    },
                    enabled = conversationWithInfoSelected.isNotEmpty(),
                    modifier = Modifier
                        .padding(vertical = AppDimens.Space.Md)
                        .size(AppDimens.ButtonSize.Sm)
                        .aspectRatio(1f)
                        .clip(shape = CircleShape),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = AppIcons.Send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(AppDimens.Icon.Sm)
                    )
                }
            }
        }
    }
}
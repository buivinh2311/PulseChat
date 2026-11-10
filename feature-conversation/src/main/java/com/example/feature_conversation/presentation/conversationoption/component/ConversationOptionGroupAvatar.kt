package com.example.feature_conversation.presentation.conversationoption.component

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_model.Group
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_utils.ImageUtil
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData
import java.io.File

@Composable
fun ConversationOptionGroupAvatar(
    modifier: Modifier = Modifier,
    context: Context,
    group: Group,
    currentUserId: String,
    onChangeGroupAvatar: (Group, File) -> Unit,
    onChangeGroupName: (Group, String) -> Unit
) {
    var showChangeGroupNameDialog by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val file = ImageUtil.uriToFile(context, uri)
            onChangeGroupAvatar(group, file)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppDimens.Space.Es,
                shape = RoundedCornerShape(AppDimens.Space.Lg)
            ),
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppDimens.Space.Lg,
                    horizontal = AppDimens.Space.Md
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = group.avatarUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.group),
                    error = painterResource(R.drawable.group),
                    modifier = Modifier
                        .size(AppDimens.ImageSize.Xxl)
                        .aspectRatio(1f)
                        .clip(shape = CircleShape)
                        .border(
                            width = AppDimens.Border.Thick,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .then(
                            if(currentUserId == group.leaderId) {
                                Modifier.clickable {
                                    launcher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            } else {
                                Modifier
                            }
                        ),
                    contentScale = ContentScale.Crop
                )

                if(currentUserId == group.leaderId) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(AppDimens.Layout.IconButtonSize)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = AppIcons.Camera,
                            contentDescription = null,
                            modifier = Modifier
                                .size(AppDimens.Icon.Sm)
                                .align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(AppDimens.Space.Md))

            Row(
                modifier = if(currentUserId == group.leaderId) {
                    Modifier.clickable {
                        showChangeGroupNameDialog = true
                    }
                } else {
                    Modifier
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if(currentUserId == group.leaderId) {
                    Spacer(modifier = Modifier.width(AppDimens.Space.Xs))
                    Icon(
                        painter = AppIcons.Edit,
                        contentDescription = "change group name",
                        modifier = Modifier.size(AppDimens.Icon.Xxs)
                    )
                }
            }
            Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_search_message),
                    icon = AppIcons.Search,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_add_member),
                    icon = AppIcons.PersonAdd,
                    iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_change_background),
                    icon = AppIcons.Paint,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_turn_off_notification),
                    icon = AppIcons.NotificationOff,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { }
                )
            }
        }
    }

    if (showChangeGroupNameDialog) {
        ChangeGroupNameDialog(
            oldGroupName = group.name,
            onDismiss = {
                showChangeGroupNameDialog = false
            },
            onChange = { groupName ->
                onChangeGroupName(group, groupName)
                showChangeGroupNameDialog = false
            }
        )
    }
}
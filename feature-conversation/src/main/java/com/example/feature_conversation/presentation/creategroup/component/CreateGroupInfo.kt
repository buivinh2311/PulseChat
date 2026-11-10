package com.example.feature_conversation.presentation.creategroup.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconButton

@Composable
fun CreateGroupInfo(
    selectedAvatarUri: String?,
    groupName: String,
    isGroupNameBlank: Boolean,
    onSelectAvatar: (Uri) -> Unit,
    onGroupNameChange: (String) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onSelectAvatar(uri)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                top = AppDimens.Space.Md,
                start = AppDimens.Space.Md,
                end = AppDimens.Space.Md
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = selectedAvatarUri,
                contentDescription = null,
                placeholder = painterResource(R.drawable.group),
                error = painterResource(R.drawable.group),
                modifier = Modifier
                    .size(AppDimens.ImageSize.Md)
                    .aspectRatio(1f)
                    .clip(shape = CircleShape)
                    .border(
                        width = AppDimens.Border.Thick - AppDimens.Border.Normal,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(AppDimens.Icon.Xxs + AppDimens.Space.Xs)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = AppIcons.Camera,
                    contentDescription = null,
                    modifier = Modifier
                        .size(AppDimens.Icon.Xxs)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            BasicTextField(
                value = groupName,
                onValueChange = { onGroupNameChange(it) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.Space.Xxl)
                    .clip(RoundedCornerShape(AppDimens.Radius.Sm))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = AppDimens.Space.Lg),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (groupName.isEmpty()) {
                            Text(
                                text = stringResource(R.string.hint_group_name),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary.copy(
                                    alpha = 0.7f
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            AnimatedVisibility(
                visible = isGroupNameBlank
            ) {
                Text(
                    text = stringResource(R.string.text_group_name_can_not_blank),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(
                            start = AppDimens.Space.Sm,
                            top = AppDimens.Space.Xs
                        )
                )
            }
        }
    }
}
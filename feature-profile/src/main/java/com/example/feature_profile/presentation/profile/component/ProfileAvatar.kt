package com.example.feature_profile.presentation.profile.component

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconTextButton
import com.example.core_utils.uriToFile
import java.io.File

@Composable
fun ProfileAvatar(
    context: Context,
    avatar: String?,
    coverPhoto: String?,
    uploadAvatar: (File) -> Unit,
    uploadCoverPhoto: (File) -> Unit,
    onNavigateToQRCode: () -> Unit
) {
    val avatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val file = uriToFile(context, uri)
            uploadAvatar(file)
        }
    }

    val coverPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val file = uriToFile(context, uri)
            uploadCoverPhoto(file)
        }
    }

    Box {
        AsyncImage(
            model = coverPhoto,
            contentDescription = null,
            placeholder = painterResource(R.drawable.cover_photo),
            error = painterResource(R.drawable.cover_photo),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.Layout.CoverPhotoHeight),
            contentScale = ContentScale.Crop
        )

        AppIconTextButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = -AppDimens.Space.Lg,
                    y = AppDimens.Space.Md
                ),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            icon = AppIcons.Camera,
            iconColor = MaterialTheme.colorScheme.primary,
            iconSize = AppDimens.Icon.Sm,
            label = stringResource(R.string.label_change_cover_photo),
            labelColor = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            onClick = {
                coverPhotoLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.ImageSize.Xxl)
                .padding(horizontal = AppDimens.Space.Md)
                .align(Alignment.BottomCenter)
                .offset(y = AppDimens.Space.El)
        ) {
            Box{
                AsyncImage(
                    model = avatar,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.account),
                    error = painterResource(R.drawable.account),
                    modifier = Modifier
                        .size(AppDimens.ImageSize.Xxl)
                        .aspectRatio(1f)
                        .clip(shape = CircleShape)
                        .border(
                            width = AppDimens.Border.Thick,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable {
                            avatarLauncher.launch(
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

            Text(
                text = "",
                modifier = Modifier.weight(1f)
            )

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = "",
                    modifier = Modifier.weight(1f)
                )

                AppIconTextButton(
                    icon = AppIcons.QRCode,
                    iconSize = AppDimens.Icon.Sm,
                    iconColor = MaterialTheme.colorScheme.primary,
                    label = stringResource(R.string.label_personal_qr_code),
                    style = MaterialTheme.typography.titleMedium,
                    onClick = {
                        onNavigateToQRCode()
                    }
                )
                Spacer(modifier = Modifier.height(AppDimens.Space.Sm))
            }
        }
    }
}
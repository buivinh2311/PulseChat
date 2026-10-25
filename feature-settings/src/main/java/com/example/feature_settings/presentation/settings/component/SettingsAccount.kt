package com.example.feature_settings.presentation.settings.component

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_model.User
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconTextButton
import com.example.core_utils.uriToFile
import java.io.File

@Composable
fun SettingsAccount(
    modifier: Modifier = Modifier,
    context: Context,
    user: User,
    uploadAvatar: (File) -> Unit,
    onNavigateToQRCode: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val file = uriToFile(context, uri)
            uploadAvatar(file)
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
                .padding(AppDimens.Space.Lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = user.avatarUrl,
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
            Spacer(modifier = Modifier.height(AppDimens.Space.Md))

            Text(
                text = user.fullName,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))

            Text(
                text = user.email,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
            ) {
                AppIconTextButton(
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primary,
                    icon = AppIcons.QRCode,
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    iconSize = AppDimens.Icon.Sm,
                    label = stringResource(R.string.label_personal_qr_code),
                    style = MaterialTheme.typography.titleMedium,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onNavigateToQRCode
                )

                AppIconTextButton(
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primary,
                    icon = AppIcons.NickName,
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    iconSize = AppDimens.Icon.Sm,
                    label = stringResource(R.string.label_edit_profile),
                    style = MaterialTheme.typography.titleMedium,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onNavigateToProfile
                )
            }
        }
    }
}
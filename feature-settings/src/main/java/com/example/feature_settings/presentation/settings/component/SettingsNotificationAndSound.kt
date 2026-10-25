package com.example.feature_settings.presentation.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.ButtonToggleItem

@Composable
fun SettingsNotificationAndSound(
    modifier: Modifier = Modifier,
    onTogglePushNotification: (Boolean) -> Unit,
    onTogglePreview: (Boolean) -> Unit,
    onToggleNotDisturb: (Boolean) -> Unit
) {
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
                .padding(vertical = AppDimens.Space.Lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_notification_and_sound),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonToggleItem(
                title = stringResource(R.string.title_notification_push),
                text = stringResource(R.string.description_notification_push),
                icon = AppIcons.NotificationPush,
                iconColor = MaterialTheme.colorScheme.primary,
                isChecked = true,
                onToggle = onTogglePushNotification
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_preview_message),
                text = stringResource(R.string.description_preview_message),
                icon = AppIcons.ShowPassword,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                isChecked = false,
                onToggle = onTogglePreview
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_not_disturb),
                text = stringResource(R.string.description_not_disturb),
                icon = AppIcons.NotDisturb,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                isChecked = true,
                onToggle = onToggleNotDisturb
            )
        }
    }
}
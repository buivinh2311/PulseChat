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
import com.example.core_ui.component.ButtonNavigateItem
import com.example.core_ui.component.ButtonToggleItem

@Composable
fun SettingsPersonalAndSecurity(
    modifier: Modifier = Modifier,
    onNavigateToPassword: () -> Unit,
    onToggleMarkAsRead: (Boolean) -> Unit,
    onToggleOnlineStatus: (Boolean) -> Unit
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
                text = stringResource(R.string.title_personal_and_security),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonNavigateItem(
                title = stringResource(R.string.title_password),
                text = stringResource(R.string.description_password),
                icon = AppIcons.Password,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = onNavigateToPassword
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_mark_as_read),
                text = stringResource(R.string.description_mark_as_read),
                icon = AppIcons.Mark,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                isChecked = false,
                onToggle = onToggleMarkAsRead
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_operational_status),
                text = stringResource(R.string.description_operational_status),
                icon = AppIcons.Status,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                isChecked = true,
                onToggle = onToggleOnlineStatus
            )
        }
    }
}
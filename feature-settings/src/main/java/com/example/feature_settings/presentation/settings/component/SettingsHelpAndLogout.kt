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

@Composable
fun SettingsHelpAndLogout(
    modifier: Modifier = Modifier,
    onNavigateToHelpCenter: () -> Unit,
    onLogout: () -> Unit
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
                text = stringResource(R.string.title_help_and_account),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonNavigateItem(
                title = stringResource(R.string.title_help_center),
                text = stringResource(R.string.description_help_center),
                icon = AppIcons.HelpCenter,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = { }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_logout),
                text = stringResource(R.string.description_logout),
                textColor = MaterialTheme.colorScheme.onErrorContainer,
                icon = AppIcons.Logout,
                iconColor = MaterialTheme.colorScheme.onErrorContainer,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                onClick = onLogout
            )
        }
    }
}
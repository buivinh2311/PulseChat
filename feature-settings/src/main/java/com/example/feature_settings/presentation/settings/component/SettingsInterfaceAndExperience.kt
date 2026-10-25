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
fun SettingsInterfaceAndExperience(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateToFontSize: () -> Unit,
    onNavigateToLanguage: () -> Unit
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
                text = stringResource(R.string.title_interface_and_experience),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonToggleItem(
                title = stringResource(R.string.title_dark_mode),
                text = stringResource(R.string.description_dark_mode),
                icon = AppIcons.DarkMode,
                iconColor = MaterialTheme.colorScheme.primary,
                isChecked = isDarkMode,
                onToggle = onToggleDarkMode
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_font_size),
                text = stringResource(R.string.description_font_size),
                icon = AppIcons.FontSize,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = onNavigateToFontSize
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_language),
                text = stringResource(R.string.description_language),
                icon = AppIcons.Language,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onNavigate = onNavigateToLanguage
            )
        }
    }
}
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
fun SettingsStorageAndData(
    modifier: Modifier = Modifier,
    onNavigateToCacheMemory: () -> Unit,
    onToggleManagerFile: (Boolean) -> Unit
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
                text = stringResource(R.string.title_storage_and_data),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonNavigateItem(
                title = stringResource(R.string.title_cache_memory_capacity),
                text = stringResource(R.string.description_cache_memory_capacity),
                icon = AppIcons.Cache,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = onNavigateToCacheMemory
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_manager_file),
                text = stringResource(R.string.description_manager_media_file),
                icon = AppIcons.AutoDownload,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                isChecked = false,
                onToggle = onToggleManagerFile
            )
        }
    }
}
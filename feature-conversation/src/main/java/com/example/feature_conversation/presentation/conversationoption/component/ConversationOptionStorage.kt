package com.example.feature_conversation.presentation.conversationoption.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ConversationOptionStorage(
    modifier: Modifier = Modifier
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
                text = stringResource(R.string.title_storage),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_auto_delete),
                text = stringResource(R.string.description_auto_delete_on),
                icon = AppIcons.AutoDelete,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = { }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_chat_capacity),
                text = stringResource(R.string.description_chat_capacity),
                icon = AppIcons.Cache,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = {}
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_delete_chat_history),
                text = stringResource(R.string.description_delete_chat_history),
                textColor = MaterialTheme.colorScheme.error,
                icon = AppIcons.Delete,
                iconColor = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { }
            )
        }
    }
}
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
import com.example.core_ui.component.ButtonToggleItem

@Composable
fun ConversationOptionGroupAction(
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
                text = stringResource(R.string.title_action),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_create_group_calendar),
                text = stringResource(R.string.description_create_group_calendar),
                icon = AppIcons.Calendar,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = { }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_create_vote),
                text = stringResource(R.string.description_create_group),
                icon = AppIcons.CreateVote,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = { }
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_pin_conversation),
                text = stringResource(R.string.description_pin_conversation),
                icon = AppIcons.Pin,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                isChecked = true,
                onToggle = { }
            )

            ButtonToggleItem(
                title = stringResource(R.string.title_hidden_conversation),
                text = stringResource(R.string.description_hidden_conversation),
                icon = AppIcons.HidePassword,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                isChecked = true,
                onToggle = { }
            )
        }
    }
}
package com.example.feature_conversation.presentation.conversationoption.component

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
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData

@Composable
fun ConversationOptionConnect(
    modifier: Modifier = Modifier,
    data: ConversationDetailData
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
                text = stringResource(R.string.title_connect),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

            ButtonNavigateItem(
                title = stringResource(R.string.title_manage_block),
                text = stringResource(R.string.description_manage_block),
                icon = AppIcons.Block,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = {}
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_report),
                text = stringResource(R.string.description_report),
                icon = AppIcons.Report,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = {}
            )

            when(data) {
                is ConversationDetailData.Direct -> {
                    ButtonNavigateItem(
                        title = stringResource(R.string.title_delete_friend),
                        text = stringResource(R.string.description_delete_friend),
                        textColor = MaterialTheme.colorScheme.error,
                        icon = AppIcons.FriendCancel,
                        iconColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        onClick = { }
                    )
                }

                is ConversationDetailData.Group -> {
                    ButtonNavigateItem(
                        title = stringResource(R.string.title_leave_group),
                        text = stringResource(R.string.description_leave_group),
                        textColor = MaterialTheme.colorScheme.error,
                        icon = AppIcons.Logout,
                        iconColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        onClick = { }
                    )
                }
            }
        }
    }
}
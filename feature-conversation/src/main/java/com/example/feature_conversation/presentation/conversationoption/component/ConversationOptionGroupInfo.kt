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
import com.example.feature_conversation.domain.model.ConversationGroupOption
import com.example.feature_conversation.domain.model.ConversationGroupOptionAction
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData

@Composable
fun ConversationOptionGroupInfo(
    modifier: Modifier = Modifier,
    data: ConversationDetailData,
    onConversationGroupOptionAction: (ConversationGroupOptionAction) -> Unit
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
                text = stringResource(R.string.title_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.Space.Md)
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_member),
                text = stringResource(R.string.description_member),
                icon = AppIcons.Member,
                iconColor = MaterialTheme.colorScheme.primary,
                onNavigate = {
                    onConversationGroupOptionAction(
                        ConversationGroupOptionAction.VIEW_MEMBER
                    )
                }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_browse_members),
                text = stringResource(R.string.description_browse_members),
                icon = AppIcons.PersonCheck,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = { }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_change_leader),
                text = stringResource(R.string.description_change_leader),
                icon = AppIcons.Leader,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onNavigate = { }
            )

            ButtonNavigateItem(
                title = stringResource(R.string.title_group_link),
                text = stringResource(R.string.description_group_link),
                icon = AppIcons.Link,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onNavigate = { }
            )
        }
    }
}
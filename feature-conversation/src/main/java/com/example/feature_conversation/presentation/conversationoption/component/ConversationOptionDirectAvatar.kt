package com.example.feature_conversation.presentation.conversationoption.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun ConversationOptionDirectAvatar(
    modifier: Modifier = Modifier,
    friend: User
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
                .padding(
                    vertical = AppDimens.Space.Lg,
                    horizontal = AppDimens.Space.Md
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = friend.avatarUrl,
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
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Md))

            Text(
                text = friend.fullName,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm)
            ) {
                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_search_message),
                    icon = AppIcons.Search,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_view_personal_profile),
                    icon = AppIcons.Account,
                    iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_change_background),
                    icon = AppIcons.Paint,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { }
                )

                ConversationOptionItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_turn_off_notification),
                    icon = AppIcons.NotificationOff,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { }
                )
            }
        }
    }
}
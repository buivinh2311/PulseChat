package com.example.feature_contacts.presentation.contacts.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R

@Composable
fun ContactsFriendSuggest(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .height(AppDimens.Layout.ChatItemHeight)
            .padding(horizontal = AppDimens.Space.Md)
            .shadow(
                elevation = AppDimens.Space.Es,
                shape = RoundedCornerShape(AppDimens.Space.Lg)
            ),
        onClick = {

        },
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = AppDimens.Space.Md,
                    end = AppDimens.Space.Xs
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.ImageSize.Md)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = AppIcons.Suggest,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(AppDimens.Icon.Md)
                )
            }
            Spacer(modifier = Modifier.width(AppDimens.Space.Md))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.title_friend_suggest),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(R.string.text_friend_suggest),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                painter = AppIcons.Next,
                contentDescription = null,
                modifier = Modifier.size(AppDimens.Icon.Sm)
            )
        }
    }
}
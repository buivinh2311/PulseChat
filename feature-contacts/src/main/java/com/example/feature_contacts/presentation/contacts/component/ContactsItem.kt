package com.example.feature_contacts.presentation.contacts.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconButton

@Composable
fun ContactsItem(
    modifier: Modifier = Modifier,
//    conversationWithInfo: ConversationWithInfo,
    onClick: (String) -> Unit
) {
//    val conversation = conversationWithInfo.conversation
//    val displayInfo = conversationWithInfo.displayInfo
    Surface(
        modifier = modifier
            .height(AppDimens.Layout.ChatItemHeight)
            .padding(horizontal = AppDimens.Space.Md)
            .shadow(
                elevation = AppDimens.Space.Es,
                shape = RoundedCornerShape(AppDimens.Space.Lg)
            ),
        onClick = {
//            onClick(conversation.id)
        },
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = AppDimens.Space.Md
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
//                    model = when (displayInfo) {
//                        is ConversationDisplayInfo.Direct -> displayInfo.otherUser.avatarUrl
//                        is ConversationDisplayInfo.Group -> displayInfo.group.avatarUrl
//                    },
                model = "",
                contentDescription = null,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                modifier = Modifier
                    .size(AppDimens.ImageSize.Md)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
//                        text = when (displayInfo) {
//                            is ConversationDisplayInfo.Direct ->
//                                displayInfo.otherUser.firstName + " " + displayInfo.otherUser.lastName
//
//                            is ConversationDisplayInfo.Group -> displayInfo.group.name
//                        },
                    text = "hus",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                AppIconButton(
                    modifier = Modifier.padding(start = AppDimens.Space.Xl),
                    icon = AppIcons.Call,
                    iconSize = AppDimens.Icon.Sm,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

                AppIconButton(
                    icon = AppIcons.Chat,
                    iconSize = AppDimens.Icon.Sm,
                    onClick = {}
                )
            }
        }
    }
}
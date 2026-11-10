package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_model.enum.SystemMessageType
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R

@Composable
fun SystemMessageItem(
    modifier: Modifier = Modifier,
    isMine: Boolean,
    actorUserName: String,
    systemMessage: Message.System,
    onView: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = AppDimens.Space.Sm,
                bottom = AppDimens.Space.Xs,
                start = AppDimens.Space.Xl,
                end = AppDimens.Space.Xl
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(AppDimens.Radius.Xl))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = AppDimens.Border.Normal,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(AppDimens.Radius.Xl)
                )
                .padding(
                    vertical = AppDimens.Space.Sm,
                    horizontal = AppDimens.Space.Lg
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = when (systemMessage.systemMessageType) {
                    SystemMessageType.CHANGE_GROUP_NAME -> AppIcons.Edit
                    SystemMessageType.PIN_MESSAGE -> AppIcons.Pin
                    SystemMessageType.UNPIN_MESSAGE -> AppIcons.Unpin
                    SystemMessageType.CREATE_GROUP -> AppIcons.CreateGroup
                    SystemMessageType.ADD_GROUP_MEMBER -> AppIcons.PersonAdd
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppDimens.Icon.Xxs)
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Xs))

            Text(
                text = if (isMine) {
                    when (systemMessage.systemMessageType) {
                        SystemMessageType.CHANGE_GROUP_NAME
                            -> stringResource(
                            R.string.text_my_last_system_message,
                            stringResource(
                                R.string.text_system_change_group_name,
                                systemMessage.text.orEmpty()
                            )
                        )

                        SystemMessageType.PIN_MESSAGE
                            -> stringResource(
                            R.string.text_my_last_system_message,
                            stringResource(R.string.text_system_pin_message)
                        )

                        SystemMessageType.UNPIN_MESSAGE
                            -> stringResource(
                            R.string.text_my_last_system_message,
                            stringResource(R.string.text_system_unpin_message)
                        )

                        SystemMessageType.CREATE_GROUP
                            -> stringResource(
                            R.string.text_my_last_system_message,
                            stringResource(R.string.text_system_create_group)
                        )

                        SystemMessageType.ADD_GROUP_MEMBER
                            -> stringResource(
                            R.string.text_my_last_system_message,
                            stringResource(
                                R.string.text_system_add_group_member,
                                systemMessage.text.orEmpty()
                            )
                        )
                    }
                } else {
                    when (systemMessage.systemMessageType) {
                        SystemMessageType.CHANGE_GROUP_NAME -> "$actorUserName " + stringResource(
                            R.string.text_system_change_group_name,
                            systemMessage.text.orEmpty()
                        )

                        SystemMessageType.PIN_MESSAGE -> "$actorUserName " +
                                stringResource(R.string.text_system_pin_message)

                        SystemMessageType.UNPIN_MESSAGE -> "$actorUserName " +
                                stringResource(R.string.text_system_unpin_message)

                        SystemMessageType.CREATE_GROUP -> "$actorUserName " +
                                stringResource(R.string.text_system_create_group)

                        SystemMessageType.ADD_GROUP_MEMBER
                            -> "$actorUserName " + stringResource(
                            R.string.text_system_add_group_member,
                            systemMessage.text.orEmpty()
                        )
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            onView?.let {
                Spacer(modifier = Modifier.width(AppDimens.Space.Sm))
                Text(
                    text = stringResource(R.string.text_view),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onView()
                    }
                )
            }
        }
    }
}
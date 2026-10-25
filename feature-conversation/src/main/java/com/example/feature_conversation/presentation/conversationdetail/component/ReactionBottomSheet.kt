package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core_model.Message
import com.example.core_model.User
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactionBottomSheet(
    currentUserId: String,
    reactionUsers: List<Pair<User, String>>,
    onDismissRequest: () -> Unit,
    onRemoveReaction: () -> Unit
) {
    val reactionsByEmoji = reactionUsers.groupBy { it.second }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = false
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            reactionsByEmoji.forEach { (emoji, users) ->
                item {
                    Text(
                        text = "$emoji ${users.size}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(
                            vertical = AppDimens.Space.Sm,
                            horizontal = AppDimens.Space.Md
                        )
                    )
                }

                items(users.size) { index ->
                    val user = users[index]
                    ReactionUserItem(
                        currentUserId = currentUserId,
                        user = user.first,
                        emoji = emoji,
                        onRemoveReaction = {
                            onRemoveReaction()
                            onDismissRequest()
                        }
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(AppDimens.Space.Md)
                    )
                }
            }
        }
    }
}

@Composable
fun ReactionUserItem(
    currentUserId: String,
    user: User,
    emoji: String,
    onRemoveReaction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if(user.id == currentUserId) {
                    Modifier.clickable {
                        onRemoveReaction()
                    }
                } else {
                    Modifier
                }
            )
            .padding(
                vertical = AppDimens.Space.Sm,
                horizontal = AppDimens.Space.Md
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(AppDimens.ImageSize.Sm)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(AppDimens.Space.Md))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.titleMedium
            )

            if(user.id == currentUserId) {
                Text(
                    text = stringResource(R.string.text_tap_to_remove),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Text(
            text = emoji,
            fontSize = 18.sp
        )
    }
}
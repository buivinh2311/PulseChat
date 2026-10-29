package com.example.feature_contacts.presentation.friendsuggestion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppBottomBar
import com.example.core_ui.component.AppButton
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.menu.AppBottomBarAction

@Composable
fun FriendSuggestionScreen(
//    friendSuggestion: List<FriendRequest>,
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_friend_suggest),
                onBack = onBack
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedAction = AppBottomBarAction.CONTACTS,
                onBottomActionClick = onBottomActionClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(vertical = AppDimens.Space.Md)
        ) {
            items(
                count = 10,
//                key = { index -> friendRequest[index].id }
            ) { index ->
//                val request = friendRequest[index]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.Space.Md)
                        .shadow(
                            elevation = AppDimens.Space.Es,
                            shape = RoundedCornerShape(AppDimens.Space.Lg)
                        ),
                    shape = RoundedCornerShape(AppDimens.Space.Lg),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = AppDimens.Space.Sm,
                                horizontal = AppDimens.Space.Md
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(AppDimens.ImageSize.Xl)
                        ) {
                            AsyncImage(
                                model = "",
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.account),
                                error = painterResource(R.drawable.account),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(
                                        end = AppDimens.Space.Xs,
                                        bottom = AppDimens.Space.Xs
                                    )
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF31A24C))
                                    .border(
                                        width = AppDimens.Border.Normal,
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = CircleShape
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(AppDimens.Space.Md))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                        ) {
                            Text(
                                text = "person/group name",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "ban chung",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    AppDimens.Space.Xs + AppDimens.Space.Xxs
                                )
                            ) {
                                AppButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(AppDimens.Layout.ButtonHeight),
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    label = stringResource(R.string.label_add_friend),
                                    labelColor = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    onClick = {}
                                )

                                AppButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(AppDimens.Layout.ButtonHeight),
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    label = stringResource(R.string.label_delete),
                                    labelColor = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.titleMedium,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }

                if(index != 9) {
                    Spacer(modifier = Modifier.height(AppDimens.Space.Sm))
                }
            }
        }
    }
}
package com.example.feature_conversation.presentation.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppBottomBar
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.menu.AppBottomBarAction
import com.example.core_ui.state.UiState
import com.example.feature_conversation.presentation.conversation.component.ConversationSearchBar
import com.example.feature_conversation.presentation.conversation.component.ConversationItem

@Composable
fun ConversationScreen(
    onNavigationToConversationDetail: (String) -> Unit,
    onSearchClick: () -> Unit,
    onScannerClick: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    val conversationViewModel: ConversationViewModel = hiltViewModel()
    val uiState by conversationViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        conversationViewModel.loadConversations()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_app_name),
                titleColor = MaterialTheme.colorScheme.primary,
                onScan = { }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedAction = AppBottomBarAction.CHAT,
                onBottomActionClick = onBottomActionClick
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            UiState.Loading -> {
                LoadingScreen()
            }

            UiState.Error -> {
                ErrorScreen(message = "ERROR")
            }

            UiState.Empty -> {

            }

            is UiState.Success -> {
                val data = state.data
                val conversationWithInfos = data.conversationWithInfos
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(
                        vertical = AppDimens.Space.Md
                    )
                ) {
                    item {
                        Column {
                            ConversationSearchBar()
//                            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))
//
//                            LazyRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm),
//                                contentPadding = PaddingValues(
//                                    start = AppDimens.Space.Md,
//                                    end = AppDimens.Space.Md,
//                                    bottom = AppDimens.Space.Xl
//                                )
//                            ) {
//                                item {
//                                    Text(
//                                        text = stringResource(R.string.label_all),
//                                        style = MaterialTheme.typography.titleMedium,
//                                        color = MaterialTheme.colorScheme.onPrimary,
//                                        modifier = Modifier
//                                            .clip(RoundedCornerShape(AppDimens.Space.Xl))
//                                            .clickable {
//
//                                            }
//                                            .background(MaterialTheme.colorScheme.primary)
//                                            .padding(
//                                                vertical = AppDimens.Space.Sm,
//                                                horizontal = AppDimens.Space.Lg
//                                            )
//                                    )
//                                    Spacer(modifier = Modifier.width(AppDimens.Space.Sm))
//
//                                    Text(
//                                        text = stringResource(R.string.label_not_read),
//                                        style = MaterialTheme.typography.titleMedium,
//                                        color = MaterialTheme.colorScheme.onSecondary,
//                                        modifier = Modifier
//                                            .clip(RoundedCornerShape(AppDimens.Space.Lg))
//                                            .clickable {
//
//                                            }
//                                            .background(MaterialTheme.colorScheme.secondary)
//                                            .padding(
//                                                vertical = AppDimens.Space.Sm,
//                                                horizontal = AppDimens.Space.Lg
//                                            )
//                                    )
//                                    Spacer(modifier = Modifier.width(AppDimens.Space.Sm))
//
//                                    Text(
//                                        text = stringResource(R.string.label_group),
//                                        style = MaterialTheme.typography.titleMedium,
//                                        color = MaterialTheme.colorScheme.onSecondary,
//                                        modifier = Modifier
//                                            .clip(RoundedCornerShape(AppDimens.Space.Lg))
//                                            .clickable {
//
//                                            }
//                                            .background(MaterialTheme.colorScheme.secondary)
//                                            .padding(
//                                                vertical = AppDimens.Space.Sm,
//                                                horizontal = AppDimens.Space.Lg
//                                            )
//                                    )
//                                }
//                            }
                        }

                        if(conversationWithInfos.any { it.isOnline }) {
                            Spacer(modifier = Modifier.height(AppDimens.Space.Md))
                        }
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Sm),
                            contentPadding = PaddingValues(
                                horizontal = AppDimens.Space.Md
                            )
                        ) {
                            items(
                                count = conversationWithInfos.size,
                                key = { index -> conversationWithInfos[index].conversation.id }
                            ) { index ->
                                val conversationWithInfo = conversationWithInfos[index]
                                val displayInfo = conversationWithInfo.displayInfo
                                if(conversationWithInfo.isOnline) {
                                    Column(
                                        modifier = Modifier.width(AppDimens.Layout.StatusWidth),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier.clickable {
                                                onNavigationToConversationDetail(
                                                    conversationWithInfo.conversation.id
                                                )
                                            }
                                        ) {
                                            AsyncImage(
                                                model = when (displayInfo) {
                                                    is ConversationDisplayInfo.Direct
                                                        -> displayInfo.otherUser.avatarUrl

                                                    is ConversationDisplayInfo.Group
                                                        -> displayInfo.group.avatarUrl
                                                },
                                                contentDescription = null,
                                                placeholder = painterResource(R.drawable.account),
                                                error = painterResource(R.drawable.account),
                                                modifier = Modifier
                                                    .size(AppDimens.ImageSize.Xl)
                                                    .clip(shape = CircleShape)
                                                    .border(
                                                        width = AppDimens.Space.Xxs,
                                                        shape = CircleShape,
                                                        color = MaterialTheme.colorScheme.primary
                                                    ),
                                                contentScale = ContentScale.Crop
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .align(alignment = Alignment.BottomEnd)
                                                    .size(AppDimens.Icon.Xxs)
                                                    .clip(shape = CircleShape)
                                                    .background(color = Color(0xFF31A24C))
                                                    .border(
                                                        width = AppDimens.Border.Thick - AppDimens.Border.Normal,
                                                        color = MaterialTheme.colorScheme.background,
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(AppDimens.Space.Sm))

                                        Text(
                                            text = when (displayInfo) {
                                                is ConversationDisplayInfo.Direct
                                                    -> displayInfo.otherUser
                                                    .fullName.split(" ").last()

                                                is ConversationDisplayInfo.Group
                                                    -> displayInfo.group.name
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(
                                AppDimens.Space.Xs + AppDimens.Space.Xxs
                            )
                        )
                    }

                    items(
                        count = conversationWithInfos.size,
                        key = { index -> conversationWithInfos[index].conversation.id }
                    ) { index ->
                        Spacer(
                            modifier = Modifier.height(
                                AppDimens.Space.Xs + AppDimens.Space.Xxs
                            )
                        )
                        ConversationItem(
                            currentUserId = data.currentUserId,
                            conversationWithInfo = conversationWithInfos[index],
                            onClick = { conversationId ->
                                onNavigationToConversationDetail(conversationId)
                            }
                        )
                    }
                }
            }
        }
    }
}
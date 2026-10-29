package com.example.feature_contacts.presentation.sentfriendrequest

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppBottomBar
import com.example.core_ui.component.AppButton
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.menu.AppBottomBarAction
import com.example.core_ui.state.UiState
import com.example.feature_contacts.presentation.friendrequest.FriendRequestEvent

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SentFriendRequestScreen(
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    val sentFriendRequestViewModel: SentFriendRequestViewModel = hiltViewModel()
    val uiState by sentFriendRequestViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sentFriendRequestViewModel.loadSentFriendRequest()
        sentFriendRequestViewModel.event.collect { event ->
            when (event) {
                SentFriendRequestEvent.ShowError -> {
                    showToast(
                        context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_friend_sent),
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
        when(val state = uiState) {
            is UiState.Loading -> {
                LoadingScreen()
            }

            is UiState.Error -> {
                ErrorScreen(title = "error")
            }

            is UiState.Empty -> {

            }

            is UiState.Success -> {
                val data = state.data
                val friendRequestWithUsers = data.friendRequestWithUsers
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(vertical = AppDimens.Space.Md)
                ) {
                    items(
                        count = 10,
                key = { index -> friendRequestWithUsers[index].request.id }
                    ) { index ->
                        val friendRequest = friendRequestWithUsers[index].request
                        val user = friendRequestWithUsers[index].user
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
                                        model = user.avatarUrl,
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
                                        text = user.fullName,
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
                                }

                                AppButton(
                                    modifier = Modifier.height(AppDimens.Layout.ButtonHeight),
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    label = stringResource(R.string.label_recall),
                                    labelColor = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    onClick = {
                                        sentFriendRequestViewModel
                                            .recallFriendRequest(friendRequest)
                                    }
                                )
                            }
                        }

                        if (index != friendRequestWithUsers.size - 1) {
                            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))
                        }
                    }
                }
            }
        }
    }
}
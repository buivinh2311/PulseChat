package com.example.feature_conversation.presentation.grouplink

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.state.UiState
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionConnect
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionDirectAction
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionDirectAvatar
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupAction
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupAvatar
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupInfo
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionItem
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionMedia
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionStorage
import com.example.feature_conversation.presentation.conversationoption.component.TurnOffNotificationBottomSheet

@Composable
fun GroupLinkScreen(
    conversationId: String,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_option),
                onBack = onBack
            )
        }
    ) { innerPadding ->
//        when (val state = uiState) {
//            UiState.Loading -> {
//                LoadingScreen()
//            }
//
//            UiState.Error -> {
//                ErrorScreen(message = "ERROR")
//            }
//
//            UiState.Empty -> {
//
//            }
//
//            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .imePadding()
                        .fillMaxSize(),
                    contentPadding = PaddingValues(AppDimens.Space.Md)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
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
                                    model = "group.avatarUrl",
                                    contentDescription = null,
                                    placeholder = painterResource(R.drawable.group),
                                    error = painterResource(R.drawable.group),
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
                                    text = "group.name",
                                    style = MaterialTheme.typography.headlineMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                                Text(
                                    text = stringResource(R.string.text_invite_link),
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Image(
                                    painter = painterResource(R.drawable.account),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(0.6f)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                                Text(
                                    text = "abc",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {},
                                        shape = RoundedCornerShape(AppDimens.Space.Lg),
                                        color = MaterialTheme.colorScheme.surfaceContainerLowest
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(AppDimens.Space.Sm),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(AppDimens.ImageSize.Xs)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = AppIcons.Copy,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(AppDimens.Icon.Xs)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))

                                            Text(
                                                text = stringResource(R.string.label_copy_link),
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {},
                                        shape = RoundedCornerShape(AppDimens.Space.Lg),
                                        color = MaterialTheme.colorScheme.surfaceContainerLowest
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(AppDimens.Space.Sm),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(AppDimens.ImageSize.Xs)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = AppIcons.Copy,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                                    modifier = Modifier.size(AppDimens.Icon.Xs)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))

                                            Text(
                                                text = stringResource(R.string.label_share_link),
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {},
                                        shape = RoundedCornerShape(AppDimens.Space.Lg),
                                        color = MaterialTheme.colorScheme.surfaceContainerLowest
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(AppDimens.Space.Sm),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(AppDimens.ImageSize.Xs)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = AppIcons.Download,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.tertiary,
                                                    modifier = Modifier.size(AppDimens.Icon.Xs)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))

                                            Text(
                                                text = stringResource(R.string.label_save_qr_code),
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }

//                        Column(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
//                        ) {
//
//                        }
                    }
                }
//            }
//        }
    }
}
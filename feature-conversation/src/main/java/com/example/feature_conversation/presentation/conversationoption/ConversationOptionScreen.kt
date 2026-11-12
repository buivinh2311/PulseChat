package com.example.feature_conversation.presentation.conversationoption

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.model.ConversationGroupOptionAction
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionConnect
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionDirectAction
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionDirectAvatar
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupAction
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupAvatar
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionGroupInfo
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionMedia
import com.example.feature_conversation.presentation.conversationoption.component.ConversationOptionStorage
import com.example.feature_conversation.presentation.conversationoption.component.TurnOffNotificationBottomSheet

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ConversationOptionScreen(
    conversationId: String,
    onBack: () -> Unit,
    onCreateGroup: (String) -> Unit,
    onViewMember: (String) -> Unit,
    onGroupLink: (String) -> Unit
) {
    val conversationOptionViewModel: ConversationOptionViewModel = hiltViewModel()
    val uiState by conversationOptionViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(conversationId) {
        conversationOptionViewModel.loadConversation(conversationId)
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        conversationOptionViewModel.event.collect { event ->
            when (event) {
                ConversationOptionEvent.ShowError -> {
                    showToast(
                        context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }
            }
        }
    }

    var showTurnOffNotification by remember {
        mutableStateOf(false)
    }

    val onConversationGroupOptionAction: (ConversationGroupOptionAction) -> Unit = { action ->
        when(action) {
            ConversationGroupOptionAction.CHANGE_BACKGROUND_IMAGE -> {}
            ConversationGroupOptionAction.TURN_OFF_NOTIFICATION -> {}
            ConversationGroupOptionAction.TURN_ON_NOTIFICATION -> {}
            ConversationGroupOptionAction.VIEW_MEDIA -> {}
            ConversationGroupOptionAction.VIEW_GROUP_CALENDAR -> {}
            ConversationGroupOptionAction.CREATE_VOTE -> {}
            ConversationGroupOptionAction.PIN -> {
                conversationOptionViewModel.pinConversation(conversationId)
            }
            ConversationGroupOptionAction.UNPIN -> {
                conversationOptionViewModel.unpinConversation(conversationId)
            }
            ConversationGroupOptionAction.GROUP_MEMBER -> onViewMember(conversationId)
            ConversationGroupOptionAction.GROUP_LINK -> onGroupLink(conversationId)

            ConversationGroupOptionAction.DELETE_CHAT_HISTORY -> {}
            ConversationGroupOptionAction.LEAVE_THE_GROUP -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_option),
                onBack = onBack
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
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .imePadding()
                        .fillMaxSize(),
                    contentPadding = PaddingValues(AppDimens.Space.Md)
                ) {
                    item {
                        val conversationOptionUiState = state.data
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Md)
                        ) {
                            when(val data = conversationOptionUiState.conversationDetailData) {
                                is ConversationDetailData.Direct -> {
                                    val friend = data.user
                                    ConversationOptionDirectAvatar(
                                        friend = friend
                                    )

                                    ConversationOptionMedia()

                                    ConversationOptionDirectAction(
                                        friend = friend,
                                        onCreateGroup = { friendId ->
                                            onCreateGroup(friendId)
                                        }
                                    )

                                    ConversationOptionStorage()

                                    ConversationOptionConnect(
                                        data = data
                                    )
                                }

                                is ConversationDetailData.Group -> {
                                    ConversationOptionGroupAvatar(
                                        context = context,
                                        group = data.group,
                                        currentUserId = conversationOptionUiState.currentUserId,
                                        onChangeGroupAvatar = { group, avatarFile ->
                                            conversationOptionViewModel
                                                .changeGroupAvatar(group, avatarFile)
                                        },
                                        onChangeGroupName = { group, groupName ->
                                            conversationOptionViewModel
                                                .changeGroupName(group, groupName)
                                        }
                                    )

                                    ConversationOptionMedia()

                                    ConversationOptionGroupAction(
                                        data = data,
                                        onConversationGroupOptionAction = onConversationGroupOptionAction
                                    )

                                    ConversationOptionGroupInfo(
                                        data = data,
                                        onConversationGroupOptionAction = onConversationGroupOptionAction
                                    )

                                    ConversationOptionStorage()

                                    ConversationOptionConnect(
                                        data = data
                                    )
                                }
                            }
                        }
                    }
                }

                if(showTurnOffNotification) {
                    TurnOffNotificationBottomSheet(
                        onDismissRequest = {
                            showTurnOffNotification = false
                        }
                    )
                }
            }
        }
    }
}
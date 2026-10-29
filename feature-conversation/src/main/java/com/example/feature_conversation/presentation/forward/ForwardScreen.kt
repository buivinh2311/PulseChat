package com.example.feature_conversation.presentation.forward

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core_model.Conversation
import com.example.core_model.User
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_model.composite.ConversationWithInfo
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppSearchBar
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.state.UiState
import com.example.feature_conversation.presentation.creategroup.component.CreateGroupInfo
import com.example.feature_conversation.presentation.creategroup.component.CreateGroupMemberBottomBar
import com.example.feature_conversation.presentation.forward.component.ForwardBottomBar
import com.example.share_presentation.PickContactItem

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ForwardScreen(
    conversationId: String,
    messageId: String,
    onBack: () -> Unit
) {
    val forwardViewModel: ForwardViewModel = hiltViewModel()
    val uiState by forwardViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        forwardViewModel.loadData(conversationId, messageId)
        forwardViewModel.event.collect { event ->
            when(event) {
                ForwardEvent.ForwardSuccess -> {
                    showToast(
                        context = context,
                        message = context.getString(R.string.text_forward_success)
                    )
                    onBack()
                }

                ForwardEvent.ShowError -> {
                    showToast(
                        context = context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }
            }
        }
    }

    var conversationWithInfoSelected: List<ConversationWithInfo> by remember {
        mutableStateOf(emptyList())
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_forward),
                subTitle = stringResource(
                    R.string.title_quantity_choose,
                    conversationWithInfoSelected.size
                ),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            UiState.Loading -> {
                LoadingScreen()
            }

            UiState.Error -> {
                ErrorScreen(title = "ERROR")
            }

            UiState.Empty -> {

            }

            is UiState.Success -> {
                val data = state.data
                conversationWithInfoSelected = data.conversationWithInfoSelected
                val conversationWithInfos = data.conversationWithInfos
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        stickyHeader {
                            AppSearchBar(
                                onSearch = { keyword ->
//                            createGroupViewModel.searchFriend(keyword, friends)
                                }
                            )
                            Spacer(modifier = Modifier.height(AppDimens.Space.Md))
                        }

                        items(
                            count = conversationWithInfos.size,
                            key = { index -> conversationWithInfos[index].conversation.id }
                        ) { index ->
                            val conversationWithInfo = conversationWithInfos[index]
                            val conversation = conversationWithInfo.conversation
                            val displayInfo = conversationWithInfo.displayInfo
                            val isChecked = conversationWithInfoSelected.any {
                                it.conversation.id == conversation.id
                            }
                            if(conversation.id != conversationId) {
                                PickContactItem(
                                    name = when (displayInfo) {
                                        is ConversationDisplayInfo.Direct
                                            -> displayInfo.otherUser.fullName

                                        is ConversationDisplayInfo.Group
                                            -> displayInfo.group.name
                                    },
                                    avatarUrl = when (displayInfo) {
                                        is ConversationDisplayInfo.Direct
                                            -> displayInfo.otherUser.avatarUrl

                                        is ConversationDisplayInfo.Group
                                            -> displayInfo.group.avatarUrl
                                    },
                                    onCheckedChange = {
                                        if (isChecked) {
                                            forwardViewModel
                                                .removeConversationWithInfo(conversationWithInfo)
                                        } else {
                                            forwardViewModel
                                                .addConversationWithInfo(conversationWithInfo)
                                        }
                                    },
                                    checked = isChecked
                                )
                                Spacer(
                                    modifier = Modifier.height(
                                        AppDimens.Space.Xs + AppDimens.Space.Xxs
                                    )
                                )
                            }
                        }
                    }

                    ForwardBottomBar(
                        message = data.message,
                        conversationWithInfoSelected = conversationWithInfoSelected,
                        onRemoveConversationWithInfo = {
                            forwardViewModel.removeConversationWithInfo(it)
                        },
                        onForward = { message, accompanyingText ->
                            forwardViewModel.forwardMessage(
                                conversationWithInfos = conversationWithInfoSelected,
                                message = message,
                                accompanyingText = accompanyingText
                            )
                        }
                    )
                }
            }
        }
    }
}
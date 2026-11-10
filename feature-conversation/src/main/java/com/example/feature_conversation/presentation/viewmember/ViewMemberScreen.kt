package com.example.feature_conversation.presentation.viewmember

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.state.UiState

@Composable
fun ViewMemberScreen(
    conversationId: String,
    onBack: () -> Unit
) {
    val viewMemberViewModel: ViewMemberViewModel = hiltViewModel()
    val uiState by viewMemberViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(conversationId) {
        viewMemberViewModel.loadConversation(conversationId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_group_member),
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
                val data = state.data
                val conversationData = data.conversationData
                val users = conversationData.users.values.toList()
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding),
                    contentPadding = PaddingValues(
                        vertical = AppDimens.Space.Md
                    )
                ) {
                    items(
                        count = users.size,
                        key = { index -> users[index].id }
                    ) { index ->
                        val user = users[index]
//                        ViewGroupMemberItem(
//                            name = user.fullName,
//                            avatarUrl = user.avatarUrl
//                        )
                    }
                }
            }
        }
    }
}
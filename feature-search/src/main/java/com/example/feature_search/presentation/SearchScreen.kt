package com.example.feature_search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_model.enum.UserStatus
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.EmptyScreen
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.menu.AppBottomBarAction
import com.example.core_ui.state.UiState
import com.example.feature_search.presentation.component.SearchTopBar
import com.example.share_presentation.SearchItem

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen (
    selectedAction: AppBottomBarAction,
    onBack: () -> Unit,
    onNavigateToConversationDetail: (String) -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    var keyword by remember {
        mutableStateOf("")
    }

    val searchViewModel: SearchViewModel = hiltViewModel()
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        searchViewModel.event.collect { event ->
            when(event) {
                is SearchEvent.NavigateToConversationDetail -> {
                    val conversationId = event.conversationId
                    onNavigateToConversationDetail(conversationId)
                }

                is SearchEvent.ShowError -> {
                    showToast(
                        context = context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                onBack = {
                    keyboardController?.hide()
                    onBack()
                },
                onQueryChange = {
                    keyword = it
                    searchViewModel.searchUser(keyword)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            state = listState
        ) {
            if(keyword.isNotEmpty()) {
                when(val state = uiState) {
                    UiState.Loading -> {
                        item { LoadingScreen() }
                    }

                    UiState.Error -> {
                        item { ErrorScreen(title = "ERROR") }
                    }

                    UiState.Empty -> {
                        item {
                            EmptyScreen(
                                modifier = Modifier.padding(innerPadding),
                                icon = AppIcons.Search,
                                title = stringResource(R.string.title_no_search)
                            )
                        }
                    }

                    is UiState.Success -> {
                        val users = state.data
                        item {
                            Spacer(modifier = Modifier.height(AppDimens.Space.Lg))
                        }

                        items(
                            count = users.size,
                            key = { index -> users[index].id }
                        ) { index ->
                            val user = users[index]
                            SearchItem(
                                id = user.id,
                                name = user.fullName,
                                avatarUrl = user.avatarUrl,
                                status = UserStatus.ONLINE,
                                onClick = {
                                    keyboardController?.hide()
                                    searchViewModel.openDirectConversation(user.id)
                                }
                            )
                        }
                    }
                }
            } else {
//                item {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(start = AppDimens.Space.Lg),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = stringResource(R.string.search_recently),
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.weight(1f)
//                        )
//
//                        TextButton(
//                            onClick = { showConfirmDialog = true }
//                        ) {
//                            Text(
//                                text = stringResource(R.string.text_delete),
//                                style = MaterialTheme.typography.titleSmall,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                    }
//                }

//                when(val state = uiState) {
//                    UiState.Loading -> {
//
//                    }
//
//                    UiState.Empty -> {
//                        item {
//                            EmptyScreen(
//                                modifier = Modifier.padding(innerPadding),
//                                icon = AppIcons.Song,
//                                title = stringResource(R.string.title_no_song_found)
//                            )
//                        }
//                    }
//
//                    is UiState.Success -> {
//                        val searchSongs = state.data
//                        items(
//                            count = searchSongs.size,
//                            key = { index -> searchSongs[index].id }
//                        ) { index ->
//                            SongItem(
//                                modifier = Modifier.padding(horizontal = AppDimens.Space.Xs),
//                                song = searchSongs[index],
//                                onSongClick = { song ->
//                                    if(isConnect) {
//                                        keyboardController?.hide()
//                                        searchViewModel.play(
//                                            queueSource = QueueSource.SEARCH,
//                                            queue = searchSongs,
//                                            startSong = song
//                                        )
//                                        onSongClick(song.id)
//                                    } else {
//                                        showToast(
//                                            context,
//                                            message = context.getString(
//                                                R.string.no_internet_message
//                                            )
//                                        )
//                                    }
//                                },
//                                onSongOptionClick = { song ->
//                                    keyboardController?.hide()
//                                    onSongOptionClick(song)
//                                }
//                            )
//                        }
//                    }
//                }
            }
        }

//        if(showConfirmDialog) {
//            DeleteConfirmDialog(
//                onDismiss = { showConfirmDialog = false },
//                onConfirm = {
//                    searchViewModel.clearAllSearchSong()
//                    showToast(
//                        context,
//                        message = context.getString(
//                            R.string.delete_search_song_success
//                        )
//                    )
//                }
//            )
//        }
    }
}
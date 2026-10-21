package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ScreenTab
import com.example.ui.FaceMeetsViewModel
import com.example.ui.components.CommentsSheet
import com.example.ui.components.FaceMeetsBottomNavigationBar
import com.example.ui.components.FaceMeetsTopAppBar
import com.example.ui.components.StoryViewerDialog
import com.example.ui.screens.*
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FaceMeetsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            // Show snackbar message if triggered
            LaunchedEffect(state.snackbarMessage) {
                state.snackbarMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearSnackbar()
                }
            }

            // System Back Handling
            BackHandler(enabled = state.activeConversation != null || state.isSearchOpen || state.currentTab != ScreenTab.FEED) {
                when {
                    state.activeConversation != null -> viewModel.closeConversation()
                    state.isSearchOpen -> viewModel.toggleSearch()
                    state.currentTab != ScreenTab.FEED -> viewModel.setTab(ScreenTab.FEED)
                }
            }

            MyApplicationTheme(darkTheme = state.isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (state.activeConversation == null && state.currentTab != ScreenTab.CAMERA) {
                                FaceMeetsTopAppBar(
                                    state = state,
                                    viewModel = viewModel
                                )
                            }
                        },
                        bottomBar = {
                            if (state.activeConversation == null && state.currentTab != ScreenTab.CAMERA) {
                                FaceMeetsBottomNavigationBar(
                                    currentTab = state.currentTab,
                                    onTabSelected = { tab -> viewModel.setTab(tab) }
                                )
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = { data ->
                                    Snackbar(
                                        snackbarData = data,
                                        containerColor = CyberTeal,
                                        contentColor = androidx.compose.ui.graphics.Color.Black
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = if (state.currentTab == ScreenTab.CAMERA) {
                                Modifier.fillMaxSize()
                            } else {
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            }
                        ) {
                            when (state.currentTab) {
                                ScreenTab.FEED -> FeedScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    onNavigateToCreate = { viewModel.setTab(ScreenTab.CREATE) },
                                    onNavigateToCamera = { viewModel.setTab(ScreenTab.CAMERA) }
                                )
                                ScreenTab.CAMERA -> CameraFilterScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                                ScreenTab.REELS -> ReelsScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                                ScreenTab.CREATE -> CreatePostScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                                ScreenTab.MESSAGES -> MessagesScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                                ScreenTab.ACTIVITY -> ActivityScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                                ScreenTab.PROFILE -> ProfileScreen(
                                    state = state,
                                    viewModel = viewModel
                                )
                            }
                        }

                        // Modal Comments BottomSheet
                        state.selectedPostForComments?.let { post ->
                            CommentsSheet(
                                post = post,
                                language = state.language,
                                onDismiss = { viewModel.closeComments() },
                                onAddComment = { text -> viewModel.addComment(post.id, text) }
                            )
                        }

                        // Fullscreen Story Viewer Dialog
                        state.activeStory?.let { story ->
                            StoryViewerDialog(
                                story = story,
                                language = state.language,
                                onDismiss = { viewModel.closeStory() },
                                onSendReply = { text ->
                                    viewModel.sendMessage(
                                        conversationId = "conv_1",
                                        text = "Replied to story: $text"
                                    )
                                }
                            )
                        }

                        // Sindhi / English Guide Dialog
                        if (state.isSindhiGuideOpen) {
                            SindhiGuideDialog(
                                language = state.language,
                                onDismiss = { viewModel.setSindhiGuideOpen(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}

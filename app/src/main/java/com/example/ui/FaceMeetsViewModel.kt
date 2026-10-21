package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class NotificationFilter {
    ALL,
    MENTIONS,
    LIKES,
    COMMENTS
}

data class FaceMeetsUiState(
    val currentTab: ScreenTab = ScreenTab.FEED,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val isDarkTheme: Boolean = true,
    val currentUser: User = SampleData.currentUser,
    val posts: List<Post> = SampleData.initialPosts(),
    val stories: List<Story> = SampleData.initialStories(),
    val reels: List<Reel> = SampleData.initialReels(),
    val conversations: List<Conversation> = SampleData.initialConversations(),
    val notifications: List<NotificationItem> = SampleData.initialNotifications(),
    val suggestedCreators: List<User> = SampleData.suggestedCreators,
    val trendingTopics: List<Pair<String, String>> = SampleData.trendingTopics,
    val activeStory: Story? = null,
    val activeConversation: Conversation? = null,
    val selectedPostForComments: Post? = null,
    val isSearchOpen: Boolean = false,
    val searchQuery: String = "",
    val activeTopicFilter: String? = null,
    val notificationFilter: NotificationFilter = NotificationFilter.ALL,
    val isSindhiGuideOpen: Boolean = false,
    val snackbarMessage: String? = null,
    // Snap / TikTok Camera Filter State
    val selectedFilter: SnapFilter = SnapFilter.BEAUTY_GLOW,
    val filterIntensity: Float = 0.85f,
    val capturedSnap: CapturedSnap? = null,
    val isFlashOn: Boolean = false,
    val isFrontCamera: Boolean = true,
    val snapTimerSeconds: Int = 0,
    val snapCaption: String = ""
)

class FaceMeetsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FaceMeetsUiState())
    val uiState: StateFlow<FaceMeetsUiState> = _uiState.asStateFlow()

    fun setTab(tab: ScreenTab) {
        _uiState.update { it.copy(currentTab = tab, activeConversation = null) }
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun toggleSearch() {
        _uiState.update { it.copy(isSearchOpen = !it.isSearchOpen, searchQuery = if (it.isSearchOpen) "" else it.searchQuery) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun filterByTopic(topic: String?) {
        _uiState.update { it.copy(activeTopicFilter = if (it.activeTopicFilter == topic) null else topic) }
    }

    fun toggleLike(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    val newIsLiked = !post.isLiked
                    val newCount = if (newIsLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
                    post.copy(isLiked = newIsLiked, likesCount = newCount)
                } else post
            }
            state.copy(
                posts = updated,
                selectedPostForComments = if (state.selectedPostForComments?.id == postId) {
                    updated.firstOrNull { it.id == postId }
                } else state.selectedPostForComments
            )
        }
    }

    fun toggleRepost(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    val newIsReposted = !post.isReposted
                    val newCount = if (newIsReposted) post.repostsCount + 1 else maxOf(0, post.repostsCount - 1)
                    post.copy(isReposted = newIsReposted, repostsCount = newCount)
                } else post
            }
            state.copy(
                posts = updated,
                snackbarMessage = Localization.get("reposted", state.language)
            )
        }
    }

    fun toggleSave(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    post.copy(isSaved = !post.isSaved)
                } else post
            }
            state.copy(posts = updated)
        }
    }

    fun sharePost(postId: String) {
        _uiState.update { state ->
            state.copy(snackbarMessage = Localization.get("shared_link", state.language))
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun openComments(post: Post) {
        _uiState.update { it.copy(selectedPostForComments = post) }
    }

    fun closeComments() {
        _uiState.update { it.copy(selectedPostForComments = null) }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "c_${UUID.randomUUID()}",
            author = _uiState.value.currentUser,
            text = text.trim(),
            timestamp = "Just now",
            likesCount = 0
        )
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        commentsCount = post.commentsCount + 1,
                        comments = listOf(newComment) + post.comments
                    )
                } else post
            }
            state.copy(
                posts = updated,
                selectedPostForComments = updated.firstOrNull { it.id == postId }
            )
        }
    }

    fun createPost(content: String, feeling: String?, tags: List<String>) {
        if (content.isBlank()) return
        val newPost = Post(
            id = "p_${UUID.randomUUID()}",
            author = _uiState.value.currentUser,
            content = content.trim(),
            timestamp = "Just now",
            likesCount = 0,
            commentsCount = 0,
            repostsCount = 0,
            feeling = feeling,
            tags = tags,
            isPinned = false
        )
        _uiState.update { state ->
            state.copy(
                posts = listOf(newPost) + state.posts,
                currentUser = state.currentUser.copy(postsCount = state.currentUser.postsCount + 1),
                currentTab = ScreenTab.FEED
            )
        }
    }

    fun openStory(story: Story) {
        _uiState.update { state ->
            val updatedStories = state.stories.map {
                if (it.id == story.id) it.copy(isViewed = true) else it
            }
            state.copy(activeStory = story, stories = updatedStories)
        }
    }

    fun closeStory() {
        _uiState.update { it.copy(activeStory = null) }
    }

    fun addStory(text: String) {
        if (text.isBlank()) return
        val newStory = Story(
            id = "s_${UUID.randomUUID()}",
            user = _uiState.value.currentUser,
            text = text.trim(),
            gradientColors = listOf(0xFF00E5FF, 0xFF9D4EDD),
            timeAgo = "Just now",
            isViewed = false
        )
        _uiState.update { state ->
            state.copy(stories = listOf(newStory) + state.stories)
        }
    }

    fun toggleReelLike(reelId: String) {
        _uiState.update { state ->
            val updated = state.reels.map { reel ->
                if (reel.id == reelId) {
                    val newIsLiked = !reel.isLiked
                    val newCount = if (newIsLiked) reel.likesCount + 1 else maxOf(0, reel.likesCount - 1)
                    reel.copy(isLiked = newIsLiked, likesCount = newCount)
                } else reel
            }
            state.copy(reels = updated)
        }
    }

    fun openConversation(conversation: Conversation) {
        _uiState.update { state ->
            val updated = state.conversations.map {
                if (it.id == conversation.id) it.copy(unreadCount = 0) else it
            }
            state.copy(activeConversation = conversation.copy(unreadCount = 0), conversations = updated)
        }
    }

    fun closeConversation() {
        _uiState.update { it.copy(activeConversation = null) }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = Message(
            id = "m_${UUID.randomUUID()}",
            senderId = _uiState.value.currentUser.id,
            text = text.trim(),
            timestamp = "Just now",
            isMe = true
        )
        _uiState.update { state ->
            val updatedConversations = state.conversations.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        lastMessage = text.trim(),
                        lastTimestamp = "Just now",
                        messages = conv.messages + newMsg
                    )
                } else conv
            }
            val active = updatedConversations.firstOrNull { it.id == conversationId }
            state.copy(
                conversations = updatedConversations,
                activeConversation = active
            )
        }
    }

    fun toggleFollowCreator(userId: String) {
        _uiState.update { state ->
            val updatedCreators = state.suggestedCreators.map { user ->
                if (user.id == userId) {
                    val newFollow = !user.isFollowing
                    val newFollowers = if (newFollow) user.followersCount + 1 else maxOf(0, user.followersCount - 1)
                    user.copy(isFollowing = newFollow, followersCount = newFollowers)
                } else user
            }
            state.copy(suggestedCreators = updatedCreators)
        }
    }

    fun updateProfile(name: String, bio: String, handle: String) {
        _uiState.update { state ->
            val updatedUser = state.currentUser.copy(
                name = name.ifBlank { state.currentUser.name },
                bio = bio.ifBlank { state.currentUser.bio },
                handle = handle.removePrefix("@").ifBlank { state.currentUser.handle }
            )
            state.copy(currentUser = updatedUser)
        }
    }

    fun setNotificationFilter(filter: NotificationFilter) {
        _uiState.update { it.copy(notificationFilter = filter) }
    }

    fun markNotificationRead(id: String) {
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
            state.copy(notifications = updated)
        }
    }

    fun setSindhiGuideOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isSindhiGuideOpen = isOpen) }
    }

    // Camera & Snap/TikTok Filters
    fun selectFilter(filter: SnapFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun setFilterIntensity(intensity: Float) {
        _uiState.update { it.copy(filterIntensity = intensity.coerceIn(0f, 1f)) }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(isFlashOn = !it.isFlashOn) }
    }

    fun toggleCameraFacing() {
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun cycleTimer() {
        _uiState.update {
            val next = when (it.snapTimerSeconds) {
                0 -> 3
                3 -> 10
                else -> 0
            }
            it.copy(snapTimerSeconds = next)
        }
    }

    fun takeSnap(imageRes: Int) {
        val snap = CapturedSnap(
            id = "snap_${UUID.randomUUID()}",
            imageRes = imageRes,
            filter = _uiState.value.selectedFilter,
            filterIntensity = _uiState.value.filterIntensity,
            bannerCaption = _uiState.value.snapCaption
        )
        _uiState.update { it.copy(capturedSnap = snap) }
    }

    fun retakeSnap() {
        _uiState.update { it.copy(capturedSnap = null, snapCaption = "") }
    }

    fun updateSnapCaption(caption: String) {
        _uiState.update { state ->
            state.copy(
                snapCaption = caption,
                capturedSnap = state.capturedSnap?.copy(bannerCaption = caption)
            )
        }
    }

    fun shareSnapToStory() {
        val snap = _uiState.value.capturedSnap ?: return
        val newStory = Story(
            id = "s_${UUID.randomUUID()}",
            user = _uiState.value.currentUser,
            text = if (snap.bannerCaption.isNotBlank()) snap.bannerCaption else "${snap.filter.iconEmoji} ${snap.filter.title} Snap!",
            gradientColors = listOf(0xFF00E5FF, 0xFF9D4EDD),
            timeAgo = "Just now",
            isViewed = false,
            imageRes = snap.imageRes
        )
        _uiState.update { state ->
            state.copy(
                stories = listOf(newStory) + state.stories,
                capturedSnap = null,
                snapCaption = "",
                currentTab = ScreenTab.FEED,
                snackbarMessage = "Snap added to your Story! 🌟"
            )
        }
    }

    fun shareSnapAsPost() {
        val snap = _uiState.value.capturedSnap ?: return
        val content = if (snap.bannerCaption.isNotBlank()) {
            "${snap.bannerCaption}\n\n✨ Filter: ${snap.filter.title} ${snap.filter.iconEmoji}"
        } else {
            "Captured with FaceMeets Snap Filter: ${snap.filter.title} ${snap.filter.iconEmoji} #SnapFilter #FaceMeets"
        }
        val newPost = Post(
            id = "p_${UUID.randomUUID()}",
            author = _uiState.value.currentUser,
            content = content,
            imageRes = snap.imageRes,
            imageLabel = "${snap.filter.title} ${snap.filter.iconEmoji}",
            timestamp = "Just now",
            likesCount = 1,
            isLiked = true,
            commentsCount = 0,
            repostsCount = 0,
            tags = listOf("SnapFilter", snap.filter.category, "FaceMeets"),
            isPinned = false
        )
        _uiState.update { state ->
            state.copy(
                posts = listOf(newPost) + state.posts,
                capturedSnap = null,
                snapCaption = "",
                currentTab = ScreenTab.FEED,
                snackbarMessage = Localization.get("reposted", state.language)
            )
        }
    }

    fun saveSnapToGallery() {
        _uiState.update { state ->
            state.copy(
                snackbarMessage = Localization.get("snap_saved", state.language)
            )
        }
    }
}

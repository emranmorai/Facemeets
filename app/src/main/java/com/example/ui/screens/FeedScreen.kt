package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Localization
import com.example.model.Post
import com.example.model.Story
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.components.PostCard
import com.example.ui.components.UserAvatar
import com.example.ui.theme.CyberTeal

@Composable
fun FeedScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier,
    onNavigateToCreate: () -> Unit = {},
    onNavigateToCamera: () -> Unit = {}
) {
    val filteredPosts = state.posts.filter { post ->
        val query = state.searchQuery.trim().lowercase()
        val matchesQuery = query.isEmpty() ||
                post.content.lowercase().contains(query) ||
                post.author.name.lowercase().contains(query) ||
                post.author.handle.lowercase().contains(query) ||
                post.tags.any { it.lowercase().contains(query) }

        val matchesTopic = state.activeTopicFilter == null ||
                post.tags.any { it.equals(state.activeTopicFilter, ignoreCase = true) }

        matchesQuery && matchesTopic
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("feed_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Topic Filter Chips Row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.activeTopicFilter == null,
                        onClick = { viewModel.filterByTopic(null) },
                        label = { Text(Localization.get("all", state.language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberTeal,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
                items(state.trendingTopics) { (tag, _) ->
                    val cleanTag = tag.removePrefix("#")
                    val isSelected = state.activeTopicFilter?.equals(cleanTag, ignoreCase = true) == true
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.filterByTopic(cleanTag) },
                        label = { Text(tag) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberTeal,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }
        }

        // Stories Row Carousel
        item {
            StoriesSection(
                state = state,
                onStoryClick = { viewModel.openStory(it) },
                onAddStoryClick = onNavigateToCamera
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Quick Create Post Input Card
        item {
            QuickCreatePostCard(
                state = state,
                onClick = onNavigateToCreate,
                onCameraClick = onNavigateToCamera
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Empty Search / Filter State
        if (filteredPosts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 44.sp
                        )
                        Text(
                            text = "No posts found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Try searching for a different keyword or topic tag.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (state.activeTopicFilter != null || state.searchQuery.isNotEmpty()) {
                            Button(
                                onClick = {
                                    viewModel.filterByTopic(null)
                                    viewModel.updateSearchQuery("")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberTeal)
                            ) {
                                Text("Clear Filters", color = Color.Black)
                            }
                        }
                    }
                }
            }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    language = state.language,
                    onLikeClick = { viewModel.toggleLike(post.id) },
                    onCommentClick = { viewModel.openComments(post) },
                    onRepostClick = { viewModel.toggleRepost(post.id) },
                    onShareClick = { viewModel.sharePost(post.id) },
                    onSaveClick = { viewModel.toggleSave(post.id) },
                    onTagClick = { tag -> viewModel.filterByTopic(tag) }
                )
            }
        }
    }
}

@Composable
fun StoriesSection(
    state: FaceMeetsUiState,
    onStoryClick: (Story) -> Unit,
    onAddStoryClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Your Story" (+ Add Story)
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .testTag("add_story_button")
                        .clickable(onClick = onAddStoryClick)
                ) {
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        UserAvatar(
                            user = state.currentUser,
                            size = 62.dp
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(CyberTeal)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = Localization.get("add_story", state.language),
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Localization.get("your_story", state.language),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Friends Stories
            items(state.stories, key = { it.id }) { story ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .testTag("story_item_${story.id}")
                        .clickable { onStoryClick(story) }
                ) {
                    UserAvatar(
                        user = story.user,
                        size = 66.dp,
                        hasStoryRing = true,
                        storyViewed = story.isViewed
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = story.user.name.split(" ").firstOrNull() ?: story.user.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (story.isViewed) FontWeight.Normal else FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (story.isViewed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun QuickCreatePostCard(
    state: FaceMeetsUiState,
    onClick: () -> Unit,
    onCameraClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
            .testTag("quick_create_post_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserAvatar(user = state.currentUser, size = 42.dp)

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = Localization.get("whats_on_mind", state.language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clickable(onClick = onCameraClick)
                        .testTag("quick_photo_filter_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = CyberTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Filters 📸", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = CyberTeal)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mood,
                        contentDescription = null,
                        tint = Color(0xFFFFB703),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(Localization.get("feeling", state.language), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = null,
                        tint = Color(0xFF9D4EDD),
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Tag", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

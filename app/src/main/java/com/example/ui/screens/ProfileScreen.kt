package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.Localization
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.components.PostCard
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.CyberTeal

enum class ProfileTab {
    POSTS,
    SAVED,
    LIKES
}

@Composable
fun ProfileScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedProfileTab by remember { mutableStateOf(ProfileTab.POSTS) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val userPosts = when (selectedProfileTab) {
        ProfileTab.POSTS -> state.posts.filter { it.author.id == state.currentUser.id }
        ProfileTab.SAVED -> state.posts.filter { it.isSaved }
        ProfileTab.LIKES -> state.posts.filter { it.isLiked }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Cover Photo Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cover_banner_1792568107879),
                    contentDescription = "Cover Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )
            }
        }

        // Profile Avatar, Details & Actions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Avatar with negative offset over cover banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.background, CircleShape)
                    ) {
                        UserAvatar(
                            user = state.currentUser,
                            size = 86.dp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(Localization.get("edit_profile", state.language))
                        }

                        IconButton(
                            onClick = { viewModel.setSindhiGuideOpen(true) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberTeal.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "FaceMeets Guide",
                                tint = CyberTeal
                            )
                        }
                    }
                }

                // Name & Bio
                Column(
                    modifier = Modifier.offset(y = (-24).dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = state.currentUser.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (state.currentUser.isVerified) {
                            VerifiedBadge(size = 20.dp)
                        }
                    }

                    Text(
                        text = "@${state.currentUser.handle}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = state.currentUser.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CyberTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = state.currentUser.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem(
                            count = state.currentUser.postsCount.toString(),
                            label = Localization.get("posts", state.language)
                        )
                        ProfileStatItem(
                            count = "${state.currentUser.followersCount}",
                            label = Localization.get("followers", state.language)
                        )
                        ProfileStatItem(
                            count = "${state.currentUser.followingCount}",
                            label = Localization.get("following", state.language)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Language and Theme Control Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Language switcher
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = CyberTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = Localization.get("language", state.language),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    AppLanguage.values().forEach { lang ->
                                        val isSelected = state.language == lang
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) CyberTeal else MaterialTheme.colorScheme.surface,
                                            modifier = Modifier
                                                .clickable { viewModel.setLanguage(lang) }
                                                .testTag("lang_chip_${lang.code}")
                                        ) {
                                            Text(
                                                text = lang.displayName,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Theme toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (state.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = null,
                                        tint = CyberTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = Localization.get("dark_mode", state.language),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Switch(
                                    checked = state.isDarkTheme,
                                    onCheckedChange = { viewModel.toggleTheme() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CyberTeal,
                                        checkedTrackColor = CyberTeal.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Profile Tabs (Posts / Saved / Likes)
                    TabRow(
                        selectedTabIndex = selectedProfileTab.ordinal,
                        containerColor = Color.Transparent,
                        contentColor = CyberTeal
                    ) {
                        Tab(
                            selected = selectedProfileTab == ProfileTab.POSTS,
                            onClick = { selectedProfileTab = ProfileTab.POSTS },
                            text = { Text(Localization.get("posts", state.language)) }
                        )
                        Tab(
                            selected = selectedProfileTab == ProfileTab.SAVED,
                            onClick = { selectedProfileTab = ProfileTab.SAVED },
                            text = { Text(Localization.get("saved", state.language)) }
                        )
                        Tab(
                            selected = selectedProfileTab == ProfileTab.LIKES,
                            onClick = { selectedProfileTab = ProfileTab.LIKES },
                            text = { Text(Localization.get("likes", state.language)) }
                        )
                    }
                }
            }
        }

        // Tab Content
        if (userPosts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No content here yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(userPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    language = state.language,
                    onLikeClick = { viewModel.toggleLike(post.id) },
                    onCommentClick = { viewModel.openComments(post) },
                    onRepostClick = { viewModel.toggleRepost(post.id) },
                    onShareClick = { viewModel.sharePost(post.id) },
                    onSaveClick = { viewModel.toggleSave(post.id) }
                )
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            user = state.currentUser,
            language = state.language,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, bio, handle ->
                viewModel.updateProfile(name, bio, handle)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditProfileDialog(
    user: com.example.model.User,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (name: String, bio: String, handle: String) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var bio by remember { mutableStateOf(user.bio) }
    var handle by remember { mutableStateOf(user.handle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Localization.get("edit_profile", language),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Localization.get("name", language)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text(Localization.get("handle", language)) },
                    prefix = { Text("@") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(Localization.get("bio", language)) },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, bio, handle) },
                colors = ButtonDefaults.buttonColors(containerColor = CyberTeal)
            ) {
                Text(Localization.get("save_changes", language), color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Localization.get("cancel", language))
            }
        }
    )
}

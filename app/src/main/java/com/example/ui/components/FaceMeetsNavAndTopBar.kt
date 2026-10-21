package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Localization
import com.example.model.ScreenTab
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.theme.CyberTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceMeetsTopAppBar(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.isSearchOpen) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text(Localization.get("search_hint", state.language), fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = CyberTeal)
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.toggleSearch() }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Search")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("feed_search_input"),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberTeal,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )
                } else {
                    BrandLogoHeader(
                        onLogoClick = { viewModel.setTab(ScreenTab.FEED) },
                        modifier = Modifier.testTag("brand_logo_header")
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Camera & Snapchat/TikTok Filters Button
                        IconButton(
                            onClick = { viewModel.setTab(ScreenTab.CAMERA) },
                            modifier = Modifier.testTag("top_bar_camera_button")
                        ) {
                            Icon(
                                imageVector = if (state.currentTab == ScreenTab.CAMERA) Icons.Filled.PhotoCamera else Icons.Outlined.PhotoCamera,
                                contentDescription = Localization.get("camera", state.language),
                                tint = CyberTeal
                            )
                        }

                        // Search Button
                        IconButton(
                            onClick = { viewModel.toggleSearch() },
                            modifier = Modifier.testTag("top_bar_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Guide button
                        IconButton(
                            onClick = { viewModel.setSindhiGuideOpen(true) },
                            modifier = Modifier.testTag("top_bar_guide_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "FaceMeets Guide",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Notifications Icon with badge
                        val unreadNotifs = state.notifications.count { !it.isRead }
                        IconButton(
                            onClick = { viewModel.setTab(ScreenTab.ACTIVITY) },
                            modifier = Modifier.testTag("top_bar_notifs_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifs > 0) {
                                        Badge(
                                            containerColor = CyberTeal,
                                            contentColor = Color.Black
                                        ) {
                                            Text(unreadNotifs.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (state.currentTab == ScreenTab.ACTIVITY) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                    contentDescription = Localization.get("activity", state.language),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Direct Messages Icon with badge
                        val unreadMessages = state.conversations.sumOf { it.unreadCount }
                        IconButton(
                            onClick = { viewModel.setTab(ScreenTab.MESSAGES) },
                            modifier = Modifier.testTag("top_bar_messages_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadMessages > 0) {
                                        Badge(
                                            containerColor = CyberTeal,
                                            contentColor = Color.Black
                                        ) {
                                            Text(unreadMessages.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (state.currentTab == ScreenTab.MESSAGES) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = Localization.get("messages", state.language),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
        }
    }
}

@Composable
fun FaceMeetsBottomNavigationBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Feed
        NavigationBarItem(
            selected = currentTab == ScreenTab.FEED,
            onClick = { onTabSelected(ScreenTab.FEED) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.FEED) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Feed"
                )
            },
            label = { Text("Feed", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyberTeal,
                indicatorColor = CyberTeal
            )
        )

        // Camera / Filters
        NavigationBarItem(
            selected = currentTab == ScreenTab.CAMERA,
            onClick = { onTabSelected(ScreenTab.CAMERA) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.CAMERA) Icons.Filled.PhotoCamera else Icons.Outlined.PhotoCamera,
                    contentDescription = "Filters"
                )
            },
            label = { Text("Filters", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyberTeal,
                indicatorColor = CyberTeal
            )
        )

        // Create Post Button
        NavigationBarItem(
            selected = currentTab == ScreenTab.CREATE,
            onClick = { onTabSelected(ScreenTab.CREATE) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CyberTeal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("Create", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = CyberTeal,
                indicatorColor = Color.Transparent
            )
        )

        // Reels
        NavigationBarItem(
            selected = currentTab == ScreenTab.REELS,
            onClick = { onTabSelected(ScreenTab.REELS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.REELS) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                    contentDescription = "Reels"
                )
            },
            label = { Text("Reels", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyberTeal,
                indicatorColor = CyberTeal
            )
        )

        // Profile
        NavigationBarItem(
            selected = currentTab == ScreenTab.PROFILE,
            onClick = { onTabSelected(ScreenTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyberTeal,
                indicatorColor = CyberTeal
            )
        )
    }
}

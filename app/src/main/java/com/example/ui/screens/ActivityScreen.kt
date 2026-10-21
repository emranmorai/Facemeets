package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Localization
import com.example.model.NotificationType
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.NotificationFilter
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.RadiantPink

@Composable
fun ActivityScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    val filtered = state.notifications.filter { item ->
        when (state.notificationFilter) {
            NotificationFilter.ALL -> true
            NotificationFilter.MENTIONS -> item.type == NotificationType.MENTION
            NotificationFilter.LIKES -> item.type == NotificationType.LIKE
            NotificationFilter.COMMENTS -> item.type == NotificationType.COMMENT
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("activity_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Notification Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.notificationFilter == NotificationFilter.ALL,
                    onClick = { viewModel.setNotificationFilter(NotificationFilter.ALL) },
                    label = { Text(Localization.get("all", state.language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberTeal,
                        selectedLabelColor = Color.Black
                    )
                )

                FilterChip(
                    selected = state.notificationFilter == NotificationFilter.LIKES,
                    onClick = { viewModel.setNotificationFilter(NotificationFilter.LIKES) },
                    label = { Text(Localization.get("likes", state.language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberTeal,
                        selectedLabelColor = Color.Black
                    )
                )

                FilterChip(
                    selected = state.notificationFilter == NotificationFilter.COMMENTS,
                    onClick = { viewModel.setNotificationFilter(NotificationFilter.COMMENTS) },
                    label = { Text(Localization.get("comments", state.language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberTeal,
                        selectedLabelColor = Color.Black
                    )
                )

                FilterChip(
                    selected = state.notificationFilter == NotificationFilter.MENTIONS,
                    onClick = { viewModel.setNotificationFilter(NotificationFilter.MENTIONS) },
                    label = { Text(Localization.get("mentions", state.language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberTeal,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        // Suggested Creators Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = Localization.get("suggested_creators", state.language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.suggestedCreators) { creator ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.width(130.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    UserAvatar(user = creator, size = 46.dp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = creator.name,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "@${creator.handle}",
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.toggleFollowCreator(creator.id) },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (creator.isFollowing) MaterialTheme.colorScheme.surfaceVariant else CyberTeal
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text(
                                            text = if (creator.isFollowing) Localization.get("following", state.language) else Localization.get("follow", state.language),
                                            fontSize = 11.sp,
                                            color = if (creator.isFollowing) MaterialTheme.colorScheme.onSurface else Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Notification Items
        items(filtered, key = { it.id }) { item ->
            val iconTint = when (item.type) {
                NotificationType.LIKE -> RadiantPink
                NotificationType.COMMENT -> CyberTeal
                NotificationType.FOLLOW -> Color(0xFF00E676)
                NotificationType.MENTION -> Color(0xFF9D4EDD)
            }

            val iconVector = when (item.type) {
                NotificationType.LIKE -> Icons.Default.Favorite
                NotificationType.COMMENT -> Icons.Default.ChatBubble
                NotificationType.FOLLOW -> Icons.Default.PersonAdd
                NotificationType.MENTION -> Icons.Default.AlternateEmail
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.markNotificationRead(item.id) }
                    .background(
                        if (!item.isRead) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        else Color.Transparent
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    UserAvatar(user = item.actor, size = 46.dp)
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(iconTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${item.actor.name} ${item.text}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (!item.isRead) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.timeAgo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!item.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CyberTeal)
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

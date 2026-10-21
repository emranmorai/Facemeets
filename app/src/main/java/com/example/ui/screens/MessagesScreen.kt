package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Conversation
import com.example.model.Localization
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.CyberTeal

@Composable
fun MessagesScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    if (state.activeConversation != null) {
        BackHandler {
            viewModel.closeConversation()
        }
        ActiveChatView(
            conversation = state.activeConversation,
            state = state,
            onBack = { viewModel.closeConversation() },
            onSendMessage = { text -> viewModel.sendMessage(state.activeConversation.id, text) },
            modifier = modifier
        )
    } else {
        ConversationListView(
            state = state,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
fun ConversationListView(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    var searchChatText by remember { mutableStateOf("") }

    val filtered = state.conversations.filter { conv ->
        searchChatText.isEmpty() ||
                conv.participant.name.contains(searchChatText, ignoreCase = true) ||
                conv.lastMessage.contains(searchChatText, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("conversations_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Search bar for messages
        item {
            OutlinedTextField(
                value = searchChatText,
                onValueChange = { searchChatText = it },
                placeholder = { Text("Search direct messages...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberTeal,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                singleLine = true
            )
        }

        // Active Online Friends
        item {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Text(
                    text = Localization.get("online", state.language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.conversations.filter { it.isOnline }) { conv ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { viewModel.openConversation(conv) }
                        ) {
                            UserAvatar(
                                user = conv.participant,
                                size = 52.dp,
                                showOnlineDot = true
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = conv.participant.name.split(" ").firstOrNull() ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Conversation items
        items(filtered, key = { it.id }) { conv ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openConversation(conv) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("conversation_row_${conv.id}"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    user = conv.participant,
                    size = 50.dp,
                    showOnlineDot = conv.isOnline
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = conv.participant.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (conv.participant.isVerified) {
                                VerifiedBadge(size = 14.dp)
                            }
                        }

                        Text(
                            text = conv.lastTimestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = conv.lastMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (conv.unreadCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (conv.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (conv.unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(CyberTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = conv.unreadCount.toString(),
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun ActiveChatView(
    conversation: Conversation,
    state: FaceMeetsUiState,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("active_chat_view")
    ) {
        // Chat Header
        Surface(
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                UserAvatar(user = conversation.participant, size = 40.dp, showOnlineDot = conversation.isOnline)

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = conversation.participant.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (conversation.participant.isVerified) {
                            VerifiedBadge(size = 14.dp)
                        }
                    }
                    Text(
                        text = if (conversation.isOnline) "Active now" else "Offline",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (conversation.isOnline) Color(0xFF00E676) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Messages Bubble Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(conversation.messages, key = { it.id }) { msg ->
                val isMe = msg.isMe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        color = if (isMe) CyberTeal else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isMe) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.timestamp,
                                fontSize = 10.sp,
                                color = if (isMe) Color.Black.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Message Input
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text(Localization.get("type_message", state.language)) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberTeal,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CyberTeal)
                        .testTag("chat_send_button"),
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = Localization.get("send", state.language),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

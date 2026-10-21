package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.components.UserAvatar
import com.example.ui.theme.CyberTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    var contentText by remember { mutableStateOf("") }
    var selectedFeeling by remember { mutableStateOf<String?>("🚀 Excited") }
    var selectedTags by remember { mutableStateOf(listOf("FaceMeets")) }
    var audience by remember { mutableStateOf("Public") }

    val feelings = listOf(
        "🚀 Excited",
        "💡 Inspired",
        "💻 Coding",
        "🎨 Creative",
        "☕ Chilling",
        "🌟 Blessed",
        "🤝 Networking"
    )

    val availableTags = listOf(
        "FaceMeets",
        "SindhTech",
        "AndroidDev",
        "KotlinCompose",
        "Design",
        "Startup",
        "FutureTech"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("create_post_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.setTab(ScreenTab.FEED) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = Localization.get("create_post", state.language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = {
                    if (contentText.isNotBlank()) {
                        viewModel.createPost(
                            content = contentText,
                            feeling = selectedFeeling,
                            tags = selectedTags
                        )
                    }
                },
                enabled = contentText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberTeal,
                    disabledContainerColor = CyberTeal.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("publish_post_button")
            ) {
                Text(
                    text = Localization.get("post_btn", state.language),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Author Card & Audience Chip
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UserAvatar(user = state.currentUser, size = 48.dp)

            Column {
                Text(
                    text = state.currentUser.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable {
                            audience = if (audience == "Public") "Friends" else "Public"
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (audience == "Public") Icons.Default.Public else Icons.Default.People,
                                contentDescription = null,
                                tint = CyberTeal,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (audience == "Public") Localization.get("public", state.language) else Localization.get("friends", state.language),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    if (selectedFeeling != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = selectedFeeling!!,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Text Input Field
        OutlinedTextField(
            value = contentText,
            onValueChange = { if (it.length <= 400) contentText = it },
            placeholder = {
                Text(
                    Localization.get("whats_on_mind", state.language),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("post_content_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberTeal,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${contentText.length}/400",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Feeling Selector
        Text(
            text = Localization.get("feeling", state.language),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(feelings) { feel ->
                val isSelected = selectedFeeling == feel
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedFeeling = if (isSelected) null else feel
                    },
                    label = { Text(feel) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberTeal,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Topic / Hashtag Selector
        Text(
            text = Localization.get("add_tags", state.language),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(availableTags) { tag ->
                val isSelected = selectedTags.contains(tag)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedTags = if (isSelected) {
                            selectedTags - tag
                        } else {
                            selectedTags + tag
                        }
                    },
                    label = { Text("#$tag") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Tip Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = CyberTeal
                )
                Text(
                    text = "Your post will be immediately visible on your feed and in search topics. Keep community conversations respectful and inspiring!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(96.dp))
    }
}

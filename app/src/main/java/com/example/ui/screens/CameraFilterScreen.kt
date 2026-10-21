package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Localization
import com.example.model.ScreenTab
import com.example.model.SnapFilter
import com.example.ui.FaceMeetsUiState
import com.example.ui.FaceMeetsViewModel
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.RadiantPink
import kotlinx.coroutines.delay

@Composable
fun CameraFilterScreen(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    if (state.capturedSnap != null) {
        CapturedSnapPreviewView(
            state = state,
            viewModel = viewModel,
            modifier = modifier
        )
    } else {
        LiveFilterCameraView(
            state = state,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
fun LiveFilterCameraView(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    var showIntensitySlider by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var countdownValue by remember { mutableStateOf<Int?>(null) }

    val categories = listOf("All", "Beauty", "Cute", "Retro", "Cyber", "Vibe", "Party")

    val filteredLenses = remember(selectedCategory) {
        if (selectedCategory == "All") SnapFilter.values().toList()
        else SnapFilter.values().filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    // Capture snap function handling countdown
    val triggerCapture = {
        if (state.snapTimerSeconds > 0) {
            countdownValue = state.snapTimerSeconds
        } else {
            viewModel.takeSnap(R.drawable.img_selfie_model_1792569769470)
        }
    }

    LaunchedEffect(countdownValue) {
        val count = countdownValue
        if (count != null && count > 0) {
            delay(1000)
            countdownValue = count - 1
        } else if (count == 0) {
            countdownValue = null
            viewModel.takeSnap(R.drawable.img_selfie_model_1792569769470)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("camera_filter_screen")
    ) {
        // Camera Viewfinder Canvas with applied filters & AR stickers
        FilterViewfinder(
            imageRes = R.drawable.img_selfie_model_1792569769470,
            filter = state.selectedFilter,
            intensity = state.filterIntensity,
            isFrontCamera = state.isFrontCamera,
            modifier = Modifier.fillMaxSize()
        )

        // Flash simulation overlay
        if (state.isFlashOn) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.15f))
            )
        }

        // Countdown Timer Overlay
        if (countdownValue != null && countdownValue!! > 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${countdownValue}",
                    color = CyberTeal,
                    fontSize = 110.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Top Camera Control Toolbar
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close / Back button
            IconButton(
                onClick = { viewModel.setTab(ScreenTab.FEED) },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Controls group (Flash, Timer, Intensity, Flip)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Intensity Slider Toggle
                IconButton(
                    onClick = { showIntensitySlider = !showIntensitySlider },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (showIntensitySlider) CyberTeal else Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Intensity",
                        tint = if (showIntensitySlider) Color.Black else Color.White
                    )
                }

                // Flash Button
                IconButton(
                    onClick = { viewModel.toggleFlash() },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (state.isFlashOn) Color(0xFFFFB703) else Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (state.isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flash",
                        tint = if (state.isFlashOn) Color.Black else Color.White
                    )
                }

                // Timer Button
                IconButton(
                    onClick = { viewModel.cycleTimer() },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (state.snapTimerSeconds > 0) CyberTeal else Color.Black.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (state.snapTimerSeconds > 0) "${state.snapTimerSeconds}s" else "⏱️",
                        color = if (state.snapTimerSeconds > 0) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Flip Camera Lens Button
                IconButton(
                    onClick = { viewModel.toggleCameraFacing() },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Flip Camera",
                        tint = Color.White
                    )
                }
            }
        }

        // Floating Intensity Slider
        AnimatedVisibility(
            visible = showIntensitySlider,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .fillMaxWidth(0.85f)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.75f),
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Localization.get("intensity", state.language),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${(state.filterIntensity * 100).toInt()}%",
                            color = CyberTeal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = state.filterIntensity,
                        onValueChange = { viewModel.setFilterIntensity(it) },
                        colors = SliderDefaults.colors(
                            thumbColor = CyberTeal,
                            activeTrackColor = CyberTeal
                        )
                    )
                }
            }
        }

        // Bottom Controls: Lens Carousel, Categories & Shutter
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) CyberTeal else Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Snapchat / TikTok Style Lens Carousel
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(filteredLenses, key = { it.id }) { filter ->
                    val isSelected = state.selectedFilter == filter
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.selectFilter(filter) }
                            .testTag("lens_item_${filter.id}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 64.dp else 52.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected)
                                        Brush.sweepGradient(listOf(CyberTeal, ElectricViolet, RadiantPink, CyberTeal))
                                    else
                                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.1f)))
                                )
                                .padding(if (isSelected) 3.dp else 1.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter.iconEmoji,
                                fontSize = if (isSelected) 28.sp else 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = filter.title,
                            color = if (isSelected) CyberTeal else Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shutter Button Row (Snapchat / TikTok ring button)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Gallery Import button
                IconButton(
                    onClick = {
                        // Cycles to sample scenic image as another canvas to test filters
                        viewModel.takeSnap(R.drawable.img_post_sample_1792568126760)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Gallery",
                        tint = Color.White
                    )
                }

                // Big Double-Ring Shutter Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(CyberTeal, ElectricViolet))
                        )
                        .clickable(onClick = triggerCapture)
                        .testTag("shutter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.selectedFilter.iconEmoji,
                        fontSize = 28.sp
                    )
                }

                // Flip Lens Button
                IconButton(
                    onClick = { viewModel.toggleCameraFacing() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun FilterViewfinder(
    imageRes: Int,
    filter: SnapFilter,
    intensity: Float,
    isFrontCamera: Boolean,
    modifier: Modifier = Modifier
) {
    val colorFilter = remember(filter, intensity) {
        when (filter) {
            SnapFilter.ORIGINAL -> null
            SnapFilter.BEAUTY_GLOW -> {
                // Soft glow peach saturation
                ColorFilter.colorMatrix(
                    ColorMatrix(
                        floatArrayOf(
                            1f + 0.15f * intensity, 0f, 0f, 0f, 15f * intensity,
                            0f, 1f + 0.08f * intensity, 0f, 0f, 10f * intensity,
                            0f, 0f, 1f + 0.05f * intensity, 0f, 5f * intensity,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            SnapFilter.NEON_CYBER -> {
                // Cyan and magenta neon punch
                ColorFilter.colorMatrix(
                    ColorMatrix(
                        floatArrayOf(
                            1.2f * intensity + (1 - intensity), 0f, 0.2f * intensity, 0f, 20f * intensity,
                            0f, 1.1f * intensity + (1 - intensity), 0.1f * intensity, 0f, 10f * intensity,
                            0.2f * intensity, 0f, 1.4f * intensity + (1 - intensity), 0f, 30f * intensity,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            SnapFilter.RETRO_VHS -> {
                // 90s warm analog VHS wash
                ColorFilter.colorMatrix(
                    ColorMatrix(
                        floatArrayOf(
                            1.1f * intensity + (1 - intensity), 0.1f * intensity, 0f, 0f, 25f * intensity,
                            0f, 0.95f * intensity + (1 - intensity), 0.05f * intensity, 0f, 15f * intensity,
                            0f, 0.1f * intensity, 0.85f * intensity + (1 - intensity), 0f, 5f * intensity,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            SnapFilter.GOLDEN_HOUR -> {
                // Warm sunset amber shift
                ColorFilter.colorMatrix(
                    ColorMatrix(
                        floatArrayOf(
                            1.3f * intensity + (1 - intensity), 0f, 0f, 0f, 35f * intensity,
                            0f, 1.1f * intensity + (1 - intensity), 0f, 0f, 20f * intensity,
                            0f, 0f, 0.8f * intensity + (1 - intensity), 0f, -10f * intensity,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            SnapFilter.NOIR_CINEMA -> {
                // High contrast black & white
                val matrix = ColorMatrix()
                matrix.setToSaturation(1f - intensity)
                ColorFilter.colorMatrix(matrix)
            }
            SnapFilter.ANIME_SKETCH -> {
                // Vibrant pop art saturation
                val matrix = ColorMatrix()
                matrix.setToSaturation(1f + 0.8f * intensity)
                ColorFilter.colorMatrix(matrix)
            }
            SnapFilter.DISCO_PARTY -> {
                ColorFilter.colorMatrix(
                    ColorMatrix(
                        floatArrayOf(
                            0.9f, 0.2f * intensity, 0.3f * intensity, 0f, 25f * intensity,
                            0.1f * intensity, 0.8f, 0.3f * intensity, 0f, 15f * intensity,
                            0.3f * intensity, 0.2f * intensity, 1.1f, 0f, 30f * intensity,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            else -> null
        }
    }

    Box(modifier = modifier) {
        // Base Photo Model
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Camera Viewfinder",
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter,
            modifier = Modifier.fillMaxSize()
        )

        // Custom Canvas AR Face Stickers & Effects Overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (filter) {
                SnapFilter.BEAUTY_GLOW -> {
                    // Soft pink blush on cheeks
                    val cheekRadius = w * 0.12f
                    val leftCheek = Offset(w * 0.38f, h * 0.44f)
                    val rightCheek = Offset(w * 0.62f, h * 0.44f)

                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0xFFFF8DA1).copy(alpha = 0.38f * intensity), Color.Transparent),
                            center = leftCheek,
                            radius = cheekRadius
                        ),
                        center = leftCheek,
                        radius = cheekRadius
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0xFFFF8DA1).copy(alpha = 0.38f * intensity), Color.Transparent),
                            center = rightCheek,
                            radius = cheekRadius
                        ),
                        center = rightCheek,
                        radius = cheekRadius
                    )
                }

                SnapFilter.CAT_EARS -> {
                    // Cute Cat Ears on top of head
                    val earSize = w * 0.18f
                    // Left Ear
                    val leftPath = Path().apply {
                        moveTo(w * 0.26f, h * 0.22f)
                        lineTo(w * 0.36f, h * 0.08f)
                        lineTo(w * 0.46f, h * 0.20f)
                        close()
                    }
                    drawPath(leftPath, color = Color(0xFFFFB703).copy(alpha = 0.9f * intensity))
                    drawPath(
                        Path().apply {
                            moveTo(w * 0.29f, h * 0.21f)
                            lineTo(w * 0.36f, h * 0.12f)
                            lineTo(w * 0.43f, h * 0.20f)
                            close()
                        },
                        color = Color(0xFFFF4081).copy(alpha = 0.8f * intensity)
                    )

                    // Right Ear
                    val rightPath = Path().apply {
                        moveTo(w * 0.54f, h * 0.20f)
                        lineTo(w * 0.64f, h * 0.08f)
                        lineTo(w * 0.74f, h * 0.22f)
                        close()
                    }
                    drawPath(rightPath, color = Color(0xFFFFB703).copy(alpha = 0.9f * intensity))
                    drawPath(
                        Path().apply {
                            moveTo(w * 0.57f, h * 0.20f)
                            lineTo(w * 0.64f, h * 0.12f)
                            lineTo(w * 0.71f, h * 0.21f)
                            close()
                        },
                        color = Color(0xFFFF4081).copy(alpha = 0.8f * intensity)
                    )

                    // Whiskers
                    val whiskerColor = Color.White.copy(alpha = 0.85f * intensity)
                    val stroke = Stroke(width = 3.dp.toPx())
                    // Left whiskers
                    drawLine(whiskerColor, Offset(w * 0.16f, h * 0.44f), Offset(w * 0.34f, h * 0.45f), strokeWidth = 3f)
                    drawLine(whiskerColor, Offset(w * 0.15f, h * 0.48f), Offset(w * 0.33f, h * 0.48f), strokeWidth = 3f)
                    // Right whiskers
                    drawLine(whiskerColor, Offset(w * 0.66f, h * 0.45f), Offset(w * 0.84f, h * 0.44f), strokeWidth = 3f)
                    drawLine(whiskerColor, Offset(w * 0.67f, h * 0.48f), Offset(w * 0.85f, h * 0.48f), strokeWidth = 3f)
                }

                SnapFilter.HEART_CROWN -> {
                    // Floating glowing hearts above head
                    val heartCenters = listOf(
                        Offset(w * 0.28f, h * 0.18f),
                        Offset(w * 0.42f, h * 0.14f),
                        Offset(w * 0.50f, h * 0.12f),
                        Offset(w * 0.58f, h * 0.14f),
                        Offset(w * 0.72f, h * 0.18f)
                    )
                    heartCenters.forEachIndexed { idx, center ->
                        val hr = (if (idx == 2) 16.dp else 12.dp).toPx() * intensity
                        drawCircle(Color(0xFFFF2A85).copy(alpha = 0.85f), center = center, radius = hr)
                    }

                    // Cheeks blush
                    val leftCheek = Offset(w * 0.38f, h * 0.44f)
                    val rightCheek = Offset(w * 0.62f, h * 0.44f)
                    drawCircle(Color(0xFFFF4081).copy(alpha = 0.35f * intensity), center = leftCheek, radius = 32.dp.toPx())
                    drawCircle(Color(0xFFFF4081).copy(alpha = 0.35f * intensity), center = rightCheek, radius = 32.dp.toPx())
                }

                SnapFilter.NEON_CYBER -> {
                    // Cyber Visor / High Tech HUD
                    val visorY = h * 0.38f
                    drawLine(
                        CyberTeal.copy(alpha = 0.85f * intensity),
                        start = Offset(w * 0.22f, visorY),
                        end = Offset(w * 0.78f, visorY),
                        strokeWidth = 4.dp.toPx()
                    )
                    // HUD Target Brackets
                    val bStroke = Stroke(width = 2.dp.toPx())
                    drawRect(
                        color = CyberTeal.copy(alpha = 0.6f * intensity),
                        topLeft = Offset(w * 0.35f, h * 0.34f),
                        size = Size(w * 0.30f, h * 0.18f),
                        style = bStroke
                    )
                }

                SnapFilter.SPARKLES -> {
                    // Glitter twinkling stars
                    val stars = listOf(
                        Offset(w * 0.22f, h * 0.28f),
                        Offset(w * 0.75f, h * 0.24f),
                        Offset(w * 0.32f, h * 0.52f),
                        Offset(w * 0.68f, h * 0.50f),
                        Offset(w * 0.50f, h * 0.22f),
                        Offset(w * 0.18f, h * 0.65f),
                        Offset(w * 0.82f, h * 0.62f)
                    )
                    stars.forEach { pos ->
                        val s = 14.dp.toPx() * intensity
                        drawLine(Color.White, Offset(pos.x - s, pos.y), Offset(pos.x + s, pos.y), strokeWidth = 2.5f)
                        drawLine(Color.White, Offset(pos.x, pos.y - s), Offset(pos.x, pos.y + s), strokeWidth = 2.5f)
                        drawCircle(Color(0xFFFFD700), center = pos, radius = 3.dp.toPx())
                    }
                }

                SnapFilter.GOLDEN_HOUR -> {
                    // Sunset Sunbeam Flare from top right
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                Color(0xFFFFB703).copy(alpha = 0.45f * intensity),
                                Color(0xFFFF8E53).copy(alpha = 0.25f * intensity),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.85f, h * 0.15f),
                            radius = w * 0.8f
                        ),
                        center = Offset(w * 0.85f, h * 0.15f),
                        radius = w * 0.8f
                    )
                }

                SnapFilter.RETRO_VHS -> {
                    // VHS scanlines
                    var y = 0f
                    while (y < h) {
                        drawLine(
                            Color.Black.copy(alpha = 0.08f * intensity),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.5f
                        )
                        y += 6.dp.toPx()
                    }
                }

                SnapFilter.DISCO_PARTY -> {
                    // Colorful Disco Hue Sweep
                    drawRect(
                        brush = Brush.sweepGradient(
                            listOf(
                                RadiantPink.copy(alpha = 0.2f * intensity),
                                CyberTeal.copy(alpha = 0.2f * intensity),
                                ElectricViolet.copy(alpha = 0.2f * intensity),
                                Color(0xFFFFB703).copy(alpha = 0.2f * intensity),
                                RadiantPink.copy(alpha = 0.2f * intensity)
                            )
                        ),
                        topLeft = Offset.Zero,
                        size = size
                    )
                }

                else -> {}
            }
        }

        // Retro VHS On-Screen Overlay Text (REC ●, SP, Date timestamp)
        if (filter == SnapFilter.RETRO_VHS) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Text(
                            text = "REC",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "SP 0:00:24",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "OCT 21 1998",
                        color = Color(0xFFFFD54F),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "01:24 PM",
                        color = Color(0xFFFFD54F),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CapturedSnapPreviewView(
    state: FaceMeetsUiState,
    viewModel: FaceMeetsViewModel,
    modifier: Modifier = Modifier
) {
    val snap = state.capturedSnap ?: return
    var captionText by remember { mutableStateOf(snap.bannerCaption) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("captured_snap_preview")
    ) {
        // Captured Image with the applied filter
        FilterViewfinder(
            imageRes = snap.imageRes,
            filter = snap.filter,
            intensity = snap.filterIntensity,
            isFrontCamera = state.isFrontCamera,
            modifier = Modifier.fillMaxSize()
        )

        // Top Navigation Bar (Retake / Close & Filter Badge)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.retakeSnap() },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .testTag("retake_snap_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Localization.get("retake", state.language),
                    tint = Color.White
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(snap.filter.iconEmoji, fontSize = 16.sp)
                    Text(
                        text = snap.filter.title,
                        color = CyberTeal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Save to Gallery
            IconButton(
                onClick = { viewModel.saveSnapToGallery() },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .testTag("save_gallery_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = Localization.get("save_gallery", state.language),
                    tint = Color.White
                )
            }
        }

        // Snapchat Style Center Black Caption Banner
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = captionText,
                onValueChange = {
                    captionText = it
                    viewModel.updateSnapCaption(it)
                },
                placeholder = {
                    Text(
                        Localization.get("add_caption_hint", state.language),
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("snap_caption_input"),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = CyberTeal
                ),
                singleLine = true
            )
        }

        // Bottom Sharing & Publishing Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Add to Story" Button (Snapchat style)
            Button(
                onClick = { viewModel.shareSnapToStory() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("share_to_story_button")
            ) {
                Icon(
                    imageVector = Icons.Default.HistoryEdu,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.get("share_story", state.language),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // "Share as Post" Button (TikTok / Instagram style)
            Button(
                onClick = { viewModel.shareSnapAsPost() },
                colors = ButtonDefaults.buttonColors(containerColor = CyberTeal),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("share_as_post_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.get("share_post", state.language),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

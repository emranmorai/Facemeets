package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.User
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.ElectricViolet

@Composable
fun UserAvatar(
    user: User,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    showOnlineDot: Boolean = false,
    hasStoryRing: Boolean = false,
    storyViewed: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val ringBrush = if (storyViewed) {
        Brush.linearGradient(listOf(Color.Gray.copy(alpha = 0.5f), Color.DarkGray))
    } else {
        Brush.sweepGradient(listOf(CyberTeal, ElectricViolet, Color(0xFFFF2A85), CyberTeal))
    }

    val boxModifier = modifier
        .size(size)
        .then(
            if (hasStoryRing) {
                Modifier
                    .border(2.5.dp, ringBrush, CircleShape)
                    .padding(3.dp)
            } else Modifier
        )
        .clip(CircleShape)
        .then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(user.avatarBgColor),
                            Color(user.avatarBgColor).copy(alpha = 0.65f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.avatarInitial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.42f).sp
            )
        }

        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.28f)
                    .clip(CircleShape)
                    .background(Color(0xFF00E676))
                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier, size: Dp = 16.dp) {
    Icon(
        imageVector = Icons.Filled.CheckCircle,
        contentDescription = "Verified User",
        tint = CyberTeal,
        modifier = modifier.size(size)
    )
}

@Composable
fun BrandLogoHeader(modifier: Modifier = Modifier, onLogoClick: (() -> Unit)? = null) {
    Row(
        modifier = modifier
            .then(if (onLogoClick != null) Modifier.clickable(onClick = onLogoClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        listOf(CyberTeal, ElectricViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text(
                text = "FaceMeets",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

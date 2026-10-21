package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.model.Localization
import com.example.ui.theme.CyberTeal

@Composable
fun SindhiGuideDialog(
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CyberTeal
                        )
                        Text(
                            text = "FaceMeets — سنڌي ۽ انگلش گائيڊ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = Localization.get("close", language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CyberTeal.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "🎉 FaceMeets Android APK هاڻي 100% Native Kotlin & Jetpack Compose ۾ تيار آهي! (FaceMeets Android APK is now 100% Native Kotlin & Jetpack Compose!)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyberTeal,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Text(
                        text = "سنڌي ۾ آسان معلومات:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "1. هي ايپ جديد Dark Indigo ۽ Cyber Teal ڊزائن تي مشتمل آهي.\n" +
                                "2. فيڊ، اسٽوريز، ريلز، ڊائريڪٽ ميسيج ۽ نوٽيفڪيشنز مڪمل ڪم ڪن ٿا.\n" +
                                "3. نئين پوسٽ ٺاهڻ، لائيڪ، ڪمينٽ باڪس ۽ شيئر لنڪ شامل آهن.\n" +
                                "4. سنڌي ۽ اردو ٻولي جي مڪمل سهولت موجود آهي.\n" +
                                "5. ايپ مان سڌو APK تيار ۽ ايڪسپورٽ ڪري سگهجي ٿي.",
                        fontSize = 13.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Text(
                        text = "English Features & Specs:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "• Fluid Native Jetpack Compose architecture\n" +
                                "• Story Carousel with interactive Story Viewer & reply bar\n" +
                                "• Post cards with animated heart like, comments bottomsheet, repost, bookmark\n" +
                                "• Discover / Reels preview with volume toggle and video marquee\n" +
                                "• Real-time Direct Messaging conversations\n" +
                                "• Profile customization: update name, handle, bio\n" +
                                "• Adaptive responsive design for mobile & tablets",
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberTeal),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("سمجهي ويس / Got it!", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

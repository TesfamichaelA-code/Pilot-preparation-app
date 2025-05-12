package com.ethiopianairlines.pilot.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ethiopianairlines.pilot.R

@Composable
fun HomeScreen(
    onExamsClick: () -> Unit = {},
    onInterviewClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onResourcesClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background - dark blue/navy color
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E2C3A))
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with title and profile icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pilot Preparation",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                // Profile icon
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_vector),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
            
            // Main image - cockpit view
            Image(
                painter = painterResource(id = R.drawable.img_cockpit),
                contentDescription = "Cockpit View",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Grid layout for menu options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeatureCard(
                        title = "Mock Exams",
                        modifier = Modifier.weight(1f),
                        onClick = onExamsClick
                    )
                    FeatureCard(
                        title = "Interview",
                        modifier = Modifier.weight(1f),
                        onClick = onInterviewClick
                    )
                }
                
                // Second row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeatureCard(
                        title = "Progress",
                        modifier = Modifier.weight(1f),
                        onClick = onProgressClick
                    )
                    FeatureCard(
                        title = "Resources",
                        modifier = Modifier.weight(1f),
                        onClick = onResourcesClick
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom navigation
            BottomNavigation()
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2E4A61))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E2C3A))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(iconResId = R.drawable.ic_home_vector, label = "Home")
        BottomNavItem(iconResId = R.drawable.ic_exams_vector, label = "Exams")
        BottomNavItem(iconResId = R.drawable.ic_interview_vector, label = "Interview")
        BottomNavItem(iconResId = R.drawable.ic_progress_vector, label = "Progress")
        BottomNavItem(iconResId = R.drawable.ic_resources_vector, label = "Resources")
    }
}

@Composable
private fun BottomNavItem(iconResId: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White
        )
    }
} 
package com.ethiopianairlines.pilot.presentation.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ethiopianairlines.pilot.R

@Composable
fun AdminHomeScreen(
    onExamsClick: () -> Unit = {},
    onInterviewClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onResourcesClick: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1B3B5A),
                modifier = Modifier.height(64.dp)
            ) {
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 }
                )
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_exam),
                            contentDescription = "Exam",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Exam") },
                    selected = selectedItem == 1,
                    onClick = { 
                        selectedItem = 1
                        onExamsClick()
                    }
                )
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_interview),
                            contentDescription = "Interview",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Interview") },
                    selected = selectedItem == 2,
                    onClick = { 
                        selectedItem = 2
                        onInterviewClick()
                    }
                )
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_progress),
                            contentDescription = "Progress",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Progress") },
                    selected = selectedItem == 3,
                    onClick = { 
                        selectedItem = 3
                        onProgressClick()
                    }
                )
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_resources),
                            contentDescription = "Resources",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Resources") },
                    selected = selectedItem == 4,
                    onClick = { 
                        selectedItem = 4
                        onResourcesClick()
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Header with profile icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pilot Preparation",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3B5A)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { /* Handle profile click */ }
                )
            }

            // Header Image
            Image(
                painter = painterResource(id = R.drawable.half_size),
                contentDescription = "Header Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdminCard(
                        title = "Update/Add\nExams",
                        modifier = Modifier.weight(1f),
                        onClick = onExamsClick
                    )
                    AdminCard(
                        title = "Update/Add\nInterview",
                        modifier = Modifier.weight(1f),
                        onClick = onInterviewClick
                    )
                }

                // Second row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdminCard(
                        title = "Track\nProgress",
                        modifier = Modifier.weight(1f),
                        onClick = onProgressClick
                    )
                    AdminCard(
                        title = "Manage\nResources",
                        modifier = Modifier.weight(1f),
                        onClick = onResourcesClick
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminCard(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B3B5A)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
} 
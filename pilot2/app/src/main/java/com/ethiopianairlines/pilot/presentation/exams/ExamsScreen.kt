package com.ethiopianairlines.pilot.presentation.exams

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ethiopianairlines.pilot.domain.model.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsScreen(
    viewModel: ExamsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onExamSelected: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val difficulties by viewModel.difficulties.collectAsState()

    var showFilters by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mock Exams") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1E2C3A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search bar
                OutlinedTextField(
                    value = viewModel.searchQuery.value,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text("Search exams...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                // Filter chips (shown when filter button is clicked)
                if (showFilters) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Categories",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = { viewModel.selectCategory(null) },
                                    label = { Text("All") }
                                )
                            }
                            items(categories) { category ->
                                FilterChip(
                                    selected = selectedCategory == category,
                                    onClick = { viewModel.selectCategory(category) },
                                    label = { Text(category) }
                                )
                            }
                        }
                        
                        Text(
                            text = "Difficulty",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedDifficulty == null,
                                    onClick = { viewModel.selectDifficulty(null) },
                                    label = { Text("All") }
                                )
                            }
                            items(difficulties) { difficulty ->
                                FilterChip(
                                    selected = selectedDifficulty == difficulty,
                                    onClick = { viewModel.selectDifficulty(difficulty) },
                                    label = { Text(difficulty) }
                                )
                            }
                        }
                    }
                }
                
                when (uiState) {
                    is ExamsUiState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                    is ExamsUiState.Success -> {
                        val exams = (uiState as ExamsUiState.Success).exams
                        if (exams.isEmpty()) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "No exams found",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(exams) { exam ->
                                    ExamCard(
                                        exam = exam,
                                        onClick = { onExamSelected(exam.id) }
                                    )
                                }
                            }
                        }
                    }
                    is ExamsUiState.Error -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = (uiState as ExamsUiState.Error).message,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamCard(
    exam: Exam,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E4A61)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exam.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                
                // Difficulty level badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(getDifficultyColor(exam.difficulty))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = exam.difficulty,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = exam.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExamInfoItem(label = "Category", value = exam.category)
                ExamInfoItem(label = "Questions", value = "${exam.questionCount}")
                ExamInfoItem(label = "Duration", value = "${exam.durationMinutes} min")
            }
        }
    }
}

@Composable
private fun LazyRow(
    horizontalArrangement: Arrangement.HorizontalOrVertical,
    modifier: Modifier = Modifier,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = horizontalArrangement as Arrangement.Horizontal,
        modifier = modifier,
        content = content
    )
}

@Composable
private fun ExamInfoItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getDifficultyColor(difficulty: String): Color {
    return when (difficulty.lowercase()) {
        "easy" -> Color(0xFF4CAF50)
        "medium" -> Color(0xFFFFA000)
        "hard" -> Color(0xFFE53935)
        else -> Color(0xFF607D8B)
    }
} 
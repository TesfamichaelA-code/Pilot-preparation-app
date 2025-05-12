package com.ethiopianairlines.pilot.presentation.exams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDetailScreen(
    examId: String,
    viewModel: ExamDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onStartExam: (String) -> Unit = {}
) {
    // Trigger loading the exam details when the screen is shown
    LaunchedEffect(examId) {
        viewModel.loadExamDetails(examId)
    }

    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            when (uiState) {
                is ExamDetailUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is ExamDetailUiState.Success -> {
                    val exam = (uiState as ExamDetailUiState.Success).exam
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Title and difficulty
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = exam.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.weight(1f)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .background(
                                        getDifficultyColor(exam.difficulty),
                                        RoundedCornerShape(4.dp)
                                    )
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
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Description
                        Text(
                            text = exam.description,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Exam details
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2E4A61)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                DetailRow(
                                    label = "Category",
                                    value = exam.category
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                DetailRow(
                                    label = "Number of Questions",
                                    value = "${exam.questionCount} questions"
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                DetailRow(
                                    icon = Icons.Default.Info,
                                    label = "Duration",
                                    value = "${exam.durationMinutes} minutes"
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                DetailRow(
                                    label = "Passing Score",
                                    value = "${exam.passingScore}%"
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Start button
                        Button(
                            onClick = { onStartExam(examId) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text(
                                text = "Start Exam",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is ExamDetailUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (uiState as ExamDetailUiState.Error).message,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = { viewModel.loadExamDetails(examId) }
                            ) {
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Text(
            text = "$label: ",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
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
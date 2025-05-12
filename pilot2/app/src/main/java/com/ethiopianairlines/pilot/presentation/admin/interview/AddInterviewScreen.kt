package com.ethiopianairlines.pilot.presentation.admin.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInterviewScreen(
    viewModel: InterviewViewModel = hiltViewModel(),
    onInterviewAdded: () -> Unit = {}
) {
    val question by viewModel.question.collectAsState()
    val sampleAnswer by viewModel.sampleAnswer.collectAsState()
    val tips by viewModel.tipsForAnswering.collectAsState()
    val year by viewModel.yearAsked.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF335870))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question", fontWeight = FontWeight.Bold, color = Color.White)
        TextField(
            value = question,
            onValueChange = { viewModel.question.value = it },
            placeholder = { Text("Interview question") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B4A5F), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Sample Answer", fontWeight = FontWeight.Bold, color = Color.White)
        TextField(
            value = sampleAnswer,
            onValueChange = { viewModel.sampleAnswer.value = it },
            placeholder = { Text("Sample Answer") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B4A5F), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Tips for Answering", fontWeight = FontWeight.Bold, color = Color.White)
        TextField(
            value = tips,
            onValueChange = { viewModel.tipsForAnswering.value = it },
            placeholder = { Text("Tips for answering") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B4A5F), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Year Asked", fontWeight = FontWeight.Bold, color = Color.White)
        TextField(
            value = year.toString(),
            onValueChange = { viewModel.yearAsked.value = it.toIntOrNull() ?: 2023 },
            placeholder = { Text("Year") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B4A5F), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.createInterview()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Add")
        }
        if (uiState is InterviewViewModel.UiState.Success) {
            onInterviewAdded()
        }
        if (uiState is InterviewViewModel.UiState.Error) {
            Text(
                text = (uiState as InterviewViewModel.UiState.Error).message,
                color = Color.Red
            )
        }
    }
} 
package com.ethiopianairlines.pilot.presentation.admin.exams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

// Define colors from Figma (or use your MaterialTheme colors)
private val ScreenBackgroundColor = Color(0xFF2A3B4D)
private val TextFieldBackgroundColor = Color(0xFF3C4F60)
private val TextColor = Color.White
private val AccentColorGreen = Color(0xFF33FF99) // For selected radio
private val ButtonPrimaryBackgroundColor = Color.White
private val ButtonPrimaryTextColor = Color(0xFF2A3B4D)
private val ButtonSecondaryBackgroundColor = Color(0xFF5A6B7D) // For "Next" button if used
private val ButtonSecondaryTextColor = Color.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditQuestionsScreen(
    examId: String, // Passed from NavGraph
    onNavigateBack: () -> Unit,
    viewModel: AddEditQuestionsViewModel = hiltViewModel()
) {
    val examDetails by viewModel.examDetails.collectAsState()
    val questionsList by viewModel.questionsList.collectAsState() // For displaying existing questions
    val addQuestionUiState by viewModel.addQuestionUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Form state from ViewModel
    val questionText = viewModel.questionText
    val options = viewModel.options // This is SnapshotStateList<String>
    val correctOptionIndex = viewModel.correctOptionIndex
    val explanationText = viewModel.explanationText

    // Handle UI state changes for snackbar messages
    LaunchedEffect(addQuestionUiState) {
        when (val currentState = addQuestionUiState) {
            is AddQuestionUiState.Success -> {
                snackbarHostState.showSnackbar(
                    message = currentState.message,
                    duration = SnackbarDuration.Short
                )
                // Reset state to Idle after showing snackbar to prevent re-triggering
                delay(1500) // Give time for snackbar to be seen
                viewModel.resetAddQuestionUiStateToIdle()
            }
            is AddQuestionUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = currentState.message,
                    duration = SnackbarDuration.Long
                )
                delay(2500)
                viewModel.resetAddQuestionUiStateToIdle()
            }
            else -> Unit
        }
    }

    Scaffold(
        containerColor = ScreenBackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        examDetails?.title ?: "Add Questions", // Use exam title
                        color = TextColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackgroundColor, // Match screen background or make distinct
                    scrolledContainerColor = ScreenBackgroundColor
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question Text Field
            SectionTitle("Question")
            OutlinedTextField(
                value = questionText.value,
                onValueChange = { questionText.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter mock exam question", color = TextColor.copy(alpha = 0.6f)) },
                shape = RoundedCornerShape(8.dp),
                colors = figmaTextFieldColors(),
                textStyle = TextStyle(color = TextColor)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Options
            SectionTitle("Options")
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .selectable(
                            selected = (index == correctOptionIndex.value),
                            onClick = { correctOptionIndex.value = index }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (index == correctOptionIndex.value),
                        onClick = { correctOptionIndex.value = index },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AccentColorGreen,
                            unselectedColor = TextColor.copy(alpha = 0.7f)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = options[index], // Access individual option state
                        onValueChange = { options[index] = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Choice ${'A' + index}", color = TextColor.copy(alpha = 0.6f)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = figmaTextFieldColors(),
                        textStyle = TextStyle(color = TextColor),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Explanation Text Field
            SectionTitle("Explanation")
            OutlinedTextField(
                value = explanationText.value,
                onValueChange = { explanationText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp),
                placeholder = { Text("The answer is... because", color = TextColor.copy(alpha = 0.6f)) },
                shape = RoundedCornerShape(8.dp),
                colors = figmaTextFieldColors(),
                textStyle = TextStyle(color = TextColor)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Or SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.addQuestion() },
                    enabled = addQuestionUiState !is AddQuestionUiState.Loading,
                    shape = RoundedCornerShape(50), // Fully rounded
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPrimaryBackgroundColor,
                        contentColor = ButtonPrimaryTextColor
                    ),
                    modifier = Modifier
                        .weight(1f) // Adjust weight as needed
                        .height(50.dp)
                ) {
                    if (addQuestionUiState is AddQuestionUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = ButtonPrimaryTextColor
                        )
                    } else {
                        Text("Add", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                // The "Next" button seems less important if "Add" clears the form.
                // If you want it:
                /*
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.addQuestion() // This will also clear the form
                    },
                    enabled = addQuestionUiState !is AddQuestionUiState.Loading,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonSecondaryBackgroundColor,
                        contentColor = ButtonSecondaryTextColor
                    ),
                    modifier = Modifier.weight(0.6f).height(50.dp)
                ) {
                    Text("Next", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                */
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Optionally, display list of added questions here
            if (questionsList.isNotEmpty()) {
                SectionTitle("Added Questions (${questionsList.size})")
                questionsList.forEach { q ->
                    Text(
                        text = "- ${q.text.take(50)}...", // Preview
                        color = TextColor.copy(alpha = 0.8f),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = TextColor,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun figmaTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = TextFieldBackgroundColor,
    unfocusedContainerColor = TextFieldBackgroundColor,
    disabledContainerColor = TextFieldBackgroundColor.copy(alpha = 0.5f),
    cursorColor = TextColor,
    focusedBorderColor = AccentColorGreen,
    unfocusedBorderColor = TextFieldBackgroundColor, // No border visible or very subtle
    focusedLabelColor = AccentColorGreen,
    unfocusedLabelColor = TextColor.copy(alpha = 0.7f),
    focusedPlaceholderColor = TextColor.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = TextColor.copy(alpha = 0.6f),
    focusedTextColor = TextColor,
    unfocusedTextColor = TextColor
)
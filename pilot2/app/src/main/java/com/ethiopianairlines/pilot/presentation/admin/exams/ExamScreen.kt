package com.ethiopianairlines.pilot.presentation.admin.exams

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay // Make sure this is imported

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    onNavigateBack: () -> Unit,
    onExamCreated: () -> Unit = {},
    viewModel: ExamViewModel = hiltViewModel() // This uses the ExamViewModel which now has ExamOperationUiState
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // These string values ("PILOT_TRAINEE", "MEDIUM") are compatible with ExamCategory.valueOf() and ExamDifficulty.valueOf()
    var category by remember { mutableStateOf("PILOT_TRAINEE") }
    var difficulty by remember { mutableStateOf("MEDIUM") }
    var durationMinutes by remember { mutableStateOf("60") }
    var isActive by remember { mutableStateOf(true) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }

    val categories = listOf("PILOT_TRAINEE", "PILOT_INSTRUCTOR", "PILOT_EXAMINER") // Ensure all enum values are here
    val difficulties = listOf("EASY", "MEDIUM", "HARD") // Ensure all enum values are here

    // Use the correct UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState() // This uiState is now ExamOperationUiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val currentState = uiState) { // Use a local variable for smart casting
            is ExamOperationUiState.Success -> { // <-- CORRECTED
                println("DEBUG: Exam operation success, calling onExamCreated()")
                snackbarHostState.showSnackbar(
                    message = "Exam created successfully!", // Or a more generic "Operation successful!" if this screen is for edit too
                    duration = SnackbarDuration.Short
                )
                delay(1000) // kotlinx.coroutines.delay
                onExamCreated() // Navigate back or refresh list
                // Optionally reset the UI state in ViewModel to Initial to prevent re-triggering on recomposition
                // viewModel.resetUiStateToInitial() // You'd need to add this method to ViewModel
            }
            is ExamOperationUiState.Error -> { // <-- CORRECTED
                snackbarHostState.showSnackbar(
                    message = "Error: ${currentState.message}",
                    duration = SnackbarDuration.Long
                )
            }
            else -> {
                // Handle Initial or Loading if needed, or do nothing
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Exam") }, // Or "Edit Exam" if it's a dual-purpose screen
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Exam Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = (uiState as? ExamOperationUiState.Error)?.message?.contains("title", ignoreCase = true) == true // Example error handling
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = category.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }.replace("Pilot ", ""), // Display friendly name
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }.replace("Pilot ", "")) },
                            onClick = {
                                category = option // Stores the "PILOT_TRAINEE" style string
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            // Difficulty Dropdown
            ExposedDropdownMenuBox(
                expanded = difficultyExpanded,
                onExpandedChange = { difficultyExpanded = it }
            ) {
                OutlinedTextField(
                    value = difficulty.lowercase().replaceFirstChar { it.uppercase() }, // Display friendly name
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Difficulty") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = difficultyExpanded,
                    onDismissRequest = { difficultyExpanded = false }
                ) {
                    difficulties.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                difficulty = option // Stores the "EASY" style string
                                difficultyExpanded = false
                            }
                        )
                    }
                }
            }

            // Duration
            OutlinedTextField(
                value = durationMinutes,
                onValueChange = { durationMinutes = it.filter { char -> char.isDigit() } }, // Allow only digits
                label = { Text("Duration (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Active Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isActive,
                    onCheckedChange = { isActive = it }
                )
            }

            // Submit Button
            Button(
                onClick = {
                    // Basic validation example
                    if (title.isBlank()) {
                        // Show snackbar or set error on TextField
                        return@Button
                    }
                    viewModel.createExam(
                        title = title,
                        description = description,
                        categoryString = category, // Pass the "PILOT_TRAINEE" style string
                        difficultyString = difficulty, // Pass the "EASY" style string
                        durationMinutes = durationMinutes.toIntOrNull() ?: 60,
                        isActive = isActive
                    )
                },
                enabled = uiState !is ExamOperationUiState.Loading, // Disable button when loading
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState is ExamOperationUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Create Exam",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Error message display (alternative to snackbar for persistent errors)
            // The LaunchedEffect already handles showing snackbar for errors.
            // This is if you want to show it directly on the screen.
            // if (uiState is ExamOperationUiState.Error) {
            //     Text(
            //         text = (uiState as ExamOperationUiState.Error).message,
            //         color = MaterialTheme.colorScheme.error,
            //         modifier = Modifier.padding(top = 8.dp)
            //     )
            // }
        }
    }
}
package com.ethiopianairlines.pilot.presentation.admin.exams

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ethiopianairlines.pilot.R
import com.ethiopianairlines.pilot.domain.model.Exam // Make sure this is your domain model

// Define your theme colors or use MaterialTheme.colorScheme
val TealDarkBlue = Color(0xFF00334E) // Example dark teal/blue
val LightBlueText = Color(0xFFB3E5FC) // Light blue for titles
val WhiteText = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListScreen(
    onNavigateBack: () -> Unit,
    onAddExamClick: () -> Unit,
    onNavigateToEditQuestions: (examId: String) -> Unit, // New navigation lambda
    viewModel: ExamListViewModel = hiltViewModel()
) {
    val exams by viewModel.exams.collectAsState()
    val uiState by viewModel.uiState.collectAsState() // This is ExamListUiState
    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var examIdToDelete by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current // For potential Toast messages or other context needs

    LaunchedEffect(Unit) {
        viewModel.loadExams()
    }

    // Handle UI state changes for snackbar messages (e.g., after delete)
    LaunchedEffect(uiState) {
        when (val currentUiState = uiState) {
            is ExamListUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = currentUiState.message,
                    duration = SnackbarDuration.Long
                )
            }
            // is ExamListUiState.Success -> { // Optional: show success after loading exams if needed
            //    snackbarHostState.showSnackbar(message = "Exams loaded", duration = SnackbarDuration.Short)
            // }
            else -> Unit
        }
    }


    if (showDeleteDialog && examIdToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                examIdToDelete = null
            },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this exam? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        examIdToDelete?.let { viewModel.deleteExam(it) }
                        showDeleteDialog = false
                        examIdToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        examIdToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mock Exams", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddExamClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exam",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Or your desired top bar color
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (uiState) {
            is ExamListUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ExamListUiState.Error -> { // Error state is now handled by LaunchedEffect for snackbar
                // You can still show a message here if you prefer it over just a snackbar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load exams. Pull to refresh or try again later.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is ExamListUiState.Initial, is ExamListUiState.Success -> {
                if (exams.isEmpty() && uiState is ExamListUiState.Success) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No exams available. Click '+' to add a new exam.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 20.dp), // Increased padding
                        verticalArrangement = Arrangement.spacedBy(20.dp),    // Increased spacing
                        horizontalArrangement = Arrangement.spacedBy(16.dp)  // Increased spacing
                    ) {
                        items(
                            items = exams,
                            key = { exam ->
                                // Provide a unique key for each item
                                // Use exam.id if it's not null or empty, otherwise use a combination
                                if (exam.id.isNotEmpty()) { // Assuming exam.id is a String
                                    exam.id
                                } else {
                                    // Fallback: Use item's hashCode or a combination of properties if ID is missing
                                    // This is a less ideal fallback, ideally IDs should be unique from the source
                                    exam.hashCode() // Less reliable, can have collisions
                                    // Or a combination of properties that might be unique enough for your case
                                    // "${exam.title}_${exams.indexOf(exam)}" // Example, still not guaranteed unique
                                }
                            }
                        ) { exam ->
                            // Your ExamGridCard composable
                            ExamGridCard(
                                exam = exam,
                                onClick = { onNavigateToEditQuestions(exam.id) },
                                onEditClick = { onNavigateToEditQuestions(exam.id) },
                                onDeleteClick = {
                                    examIdToDelete = exam.id
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamGridCard(
    exam: Exam,
    onClick: () -> Unit, // For clicking the whole card
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card( // Using Card for elevation and shape
        modifier = Modifier
            .aspectRatio(0.85f) // Adjust aspect ratio for a slightly taller card
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp), // More rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.exam_placeholder), // Replace with dynamic image if available
                contentDescription = exam.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            // Darker, slightly more opaque gradient overlay for better text readability
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f // Start gradient a bit lower
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 10.dp), // Adjusted padding
                verticalArrangement = Arrangement.SpaceBetween // Pushes content to top and bottom
            ) {
                // Title - styled to match example
                Text(
                    text = exam.title, // Removed the angle brackets for a cleaner look, add back if preferred
                    color = WhiteText, // Was LightBlueText, changed to White for better contrast on image
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, // Slightly larger
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle - Centered (can be removed if not needed, or use exam.description)
                Column(
                    modifier = Modifier.weight(1f), // Takes up available space to center
                    verticalArrangement = Arrangement.Bottom, // Pushes text to bottom of this column
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Are you ready?\nBegin your exam!", // Or exam.description
                        color = WhiteText.copy(alpha = 0.9f), // Slightly less prominent
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }


                // Action Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(36.dp)) { // Slightly smaller icon button
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Exam",
                            tint = WhiteText
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp)) // Space between icons
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete, // Using Outline version
                            contentDescription = "Delete Exam",
                            tint = WhiteText
                        )
                    }
                }
            }
        }
    }
}
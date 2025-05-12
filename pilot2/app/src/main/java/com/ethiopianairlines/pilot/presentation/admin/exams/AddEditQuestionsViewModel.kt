package com.ethiopianairlines.pilot.presentation.admin.exams

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.domain.model.Exam // For exam title, if needed
import com.ethiopianairlines.pilot.domain.model.Question
import com.ethiopianairlines.pilot.domain.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddQuestionUiState {
    object Idle : AddQuestionUiState()
    object Loading : AddQuestionUiState()
    data class Success(val message: String) : AddQuestionUiState()
    data class Error(val message: String) : AddQuestionUiState()
}

@HiltViewModel
class AddEditQuestionsViewModel @Inject constructor(
    private val examRepository: ExamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val examId: String = savedStateHandle.get<String>("examId") ?: ""

    private val _examDetails = MutableStateFlow<Exam?>(null) // To store exam title if needed
    val examDetails: StateFlow<Exam?> = _examDetails.asStateFlow()

    private val _questionsList = MutableStateFlow<List<Question>>(emptyList())
    val questionsList: StateFlow<List<Question>> = _questionsList.asStateFlow()

    private val _addQuestionUiState = MutableStateFlow<AddQuestionUiState>(AddQuestionUiState.Idle)
    val addQuestionUiState: StateFlow<AddQuestionUiState> = _addQuestionUiState.asStateFlow()

    // Form State
    val questionText = mutableStateOf("")
    val options = mutableStateListOf("", "", "", "") // Assuming 4 options
    val correctOptionIndex = mutableStateOf(0) // Default to first option
    val explanationText = mutableStateOf("")

    init {
        if (examId.isNotBlank()) {
            loadExamDetails()
            loadQuestions()
        } else {
            _addQuestionUiState.value = AddQuestionUiState.Error("Exam ID is missing.")
        }
    }

    private fun loadExamDetails() {
        viewModelScope.launch {
            try {
                val exam = examRepository.getExamById(examId) // Assuming this fetches the exam shell
                _examDetails.value = exam
            } catch (e: Exception) {
                // Handle error loading exam details if necessary
                _addQuestionUiState.value = AddQuestionUiState.Error("Failed to load exam details: ${e.message}")
            }
        }
    }


    fun loadQuestions() {
        if (examId.isBlank()) return
        viewModelScope.launch {
            examRepository.getQuestionsForExam(examId)
                .catch { e ->
                    _addQuestionUiState.value = AddQuestionUiState.Error("Failed to load questions: ${e.message}")
                }
                .collect { questions ->
                    _questionsList.value = questions
                }
        }
    }

    fun addQuestion() {
        if (examId.isBlank()) {
            _addQuestionUiState.value = AddQuestionUiState.Error("Cannot add question: Exam ID is missing.")
            return
        }
        if (questionText.value.isBlank()) {
            _addQuestionUiState.value = AddQuestionUiState.Error("Question text cannot be empty.")
            return
        }
        if (options.any { it.isBlank() }) {
            _addQuestionUiState.value = AddQuestionUiState.Error("All options must be filled.")
            return
        }

        viewModelScope.launch {
            _addQuestionUiState.value = AddQuestionUiState.Loading
            try {
                val newQuestion = Question(
                    examId = examId,
                    text = questionText.value,
                    options = options.toList(), // Convert SnapshotStateList to List
                    correctAnswerIndex = correctOptionIndex.value,
                    explanation = explanationText.value.ifBlank { null }
                )
                val addedQuestion = examRepository.addQuestionToExam(examId, newQuestion)
                _addQuestionUiState.value = AddQuestionUiState.Success("Question added successfully!")
                // Refresh list or add to existing
                // _questionsList.value = _questionsList.value + addedQuestion // Optimistic update
                loadQuestions() // Re-fetch to be sure
                clearFormForNextQuestion()
            } catch (e: Exception) {
                _addQuestionUiState.value = AddQuestionUiState.Error("Failed to add question: ${e.message}")
            }
        }
    }

    fun clearFormForNextQuestion() {
        questionText.value = ""
        options.forEachIndexed { index, _ -> options[index] = "" }
        correctOptionIndex.value = 0
        explanationText.value = ""
        // Set UI state back to Idle after a short delay or let user interaction do it
        // viewModelScope.launch {
        //     kotlinx.coroutines.delay(2000)
        //     if (_addQuestionUiState.value is AddQuestionUiState.Success) {
        //         _addQuestionUiState.value = AddQuestionUiState.Idle
        //     }
        // }
    }

    fun resetAddQuestionUiStateToIdle() {
        _addQuestionUiState.value = AddQuestionUiState.Idle
    }
}
package com.ethiopianairlines.pilot.presentation.admin.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.data.mapper.ExamMapper
import com.ethiopianairlines.pilot.domain.model.Exam
import com.ethiopianairlines.pilot.domain.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ExamListUiState {
    object Initial : ExamListUiState()
    object Loading : ExamListUiState()
    object Success : ExamListUiState()
    data class Error(val message: String) : ExamListUiState()
}

@HiltViewModel
class ExamListViewModel @Inject constructor(
    private val examRepository: ExamRepository // Injects the interface
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExamListUiState>(ExamListUiState.Initial)
    val uiState: StateFlow<ExamListUiState> = _uiState.asStateFlow()

    private val _exams = MutableStateFlow<List<Exam>>(emptyList()) // List of DomainExam
    val exams: StateFlow<List<Exam>> = _exams.asStateFlow()

    fun loadExams() {
        viewModelScope.launch {
            _uiState.value = ExamListUiState.Loading
            try {
                // examRepository.getExams() now directly returns Flow<List<DomainExam>>
                examRepository.getExams()
                    .collect { domainExams -> // domainExams is already List<DomainExam>
                        _exams.value = domainExams
                        _uiState.value = ExamListUiState.Success
                    }
            } catch (e: Exception) {
                _uiState.value = ExamListUiState.Error(e.message ?: "Failed to load exams")
            }
        }
    }

    // deleteExam and editExam remain the same conceptually
    // Inside ExamListViewModel.kt

// ... (other states)
// You might want specific states for delete if you want to show a specific message
// For now, we'll assume loadExams() handles refreshing the list and UI state.

    fun deleteExam(examId: String) {
        viewModelScope.launch {
            // Optional: Set a specific loading state for delete
            // _uiState.value = ExamListUiState.Loading // Or a new Deleting state
            try {
                examRepository.deleteExam(examId)
                // examRepository.getExams() should be re-fetched or list modified locally
                // Calling loadExams() will re-fetch and update UI state to Success or Error
                loadExams() // This will set _uiState to Success (if load successful) or Error
            } catch (e: Exception) {
                _uiState.value = ExamListUiState.Error(e.message ?: "Failed to delete exam")
            }
        }
    }

// The editExam function in ViewModel might become redundant if navigation is purely a UI concern.
// If it does any pre-navigation logic, keep it. Otherwise, it can be removed.
// For this example, we'll assume navigation is handled by the screen.
// fun editExam(exam: Exam) {
//     // This will be handled by navigation to the edit screen
// }

    fun editExam(exam: Exam) {
        // Navigation logic, no change needed here for this fix
    }
}
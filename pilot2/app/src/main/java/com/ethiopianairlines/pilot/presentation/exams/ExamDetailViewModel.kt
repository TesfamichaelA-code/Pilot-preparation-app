package com.ethiopianairlines.pilot.presentation.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.domain.model.Exam
import com.ethiopianairlines.pilot.domain.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ExamDetailUiState {
    object Loading : ExamDetailUiState()
    data class Success(val exam: Exam) : ExamDetailUiState()
    data class Error(val message: String) : ExamDetailUiState()
}

@HiltViewModel
class ExamDetailViewModel @Inject constructor(
    private val examRepository: ExamRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ExamDetailUiState>(ExamDetailUiState.Loading)
    val uiState: StateFlow<ExamDetailUiState> = _uiState.asStateFlow()
    
    fun loadExamDetails(examId: String) {
        viewModelScope.launch {
            _uiState.value = ExamDetailUiState.Loading
            try {
                val exam = examRepository.getExamById(examId)
                if (exam != null) {
                    _uiState.value = ExamDetailUiState.Success(exam)
                } else {
                    _uiState.value = ExamDetailUiState.Error("Exam not found")
                }
            } catch (e: Exception) {
                // Log detailed exception information
                android.util.Log.e("ExamDetailViewModel", "Error loading exam $examId", e)
                val errorMessage = when {
                    e.message?.contains("NullPointerException") == true -> "Error parsing exam data. Please check API response format."
                    e.message?.contains("Network") == true -> "Network error. Please check your connection."
                    else -> e.message ?: "Failed to load exam details"
                }
                _uiState.value = ExamDetailUiState.Error(errorMessage)
            }
        }
    }
} 
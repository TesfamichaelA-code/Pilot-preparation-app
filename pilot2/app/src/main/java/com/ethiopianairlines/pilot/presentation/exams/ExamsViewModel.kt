package com.ethiopianairlines.pilot.presentation.exams

import androidx.compose.runtime.mutableStateOf
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

sealed class ExamsUiState {
    object Loading : ExamsUiState()
    data class Success(val exams: List<Exam>) : ExamsUiState()
    data class Error(val message: String) : ExamsUiState()
}

@HiltViewModel
class ExamsViewModel @Inject constructor(
    private val examRepository: ExamRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ExamsUiState>(ExamsUiState.Loading)
    val uiState: StateFlow<ExamsUiState> = _uiState.asStateFlow()
    
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()
    
    private val _difficulties = MutableStateFlow<List<String>>(emptyList())
    val difficulties: StateFlow<List<String>> = _difficulties.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    val selectedDifficulty: StateFlow<String?> = _selectedDifficulty.asStateFlow()
    
    val searchQuery = mutableStateOf("")
    
    private var allExams: List<Exam> = emptyList()
    
    init {
        loadExams()
        loadCategories()
        loadDifficulties()
    }
    
    fun loadExams() {
        viewModelScope.launch {
            _uiState.value = ExamsUiState.Loading
            try {
                val exams = examRepository.getExams(
                    category = _selectedCategory.value,
                    difficulty = _selectedDifficulty.value
                )
                allExams = exams
                filterExams()
                _uiState.value = ExamsUiState.Success(filterExams())
            } catch (e: Exception) {
                _uiState.value = ExamsUiState.Error(e.message ?: "Failed to load exams")
            }
        }
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = examRepository.getExamCategories()
                _categories.value = categories
            } catch (e: Exception) {
                // Handle error, maybe set a message in the UI state
            }
        }
    }
    
    private fun loadDifficulties() {
        // Usually a fixed set, but could be fetched from the backend as well
        _difficulties.value = listOf("Easy", "Medium", "Hard")
    }
    
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        loadExams()
    }
    
    fun selectDifficulty(difficulty: String?) {
        _selectedDifficulty.value = difficulty
        loadExams()
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        if (_uiState.value is ExamsUiState.Success) {
            _uiState.value = ExamsUiState.Success(filterExams())
        }
    }
    
    private fun filterExams(): List<Exam> {
        val query = searchQuery.value.lowercase()
        return if (query.isBlank()) {
            allExams
        } else {
            allExams.filter {
                it.title.lowercase().contains(query) ||
                it.description.lowercase().contains(query) ||
                it.category.lowercase().contains(query)
            }
        }
    }
} 
package com.ethiopianairlines.pilot.presentation.admin.interview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.data.model.InterviewRequest
import com.ethiopianairlines.pilot.data.repository.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    private val repository: InterviewRepository
) : ViewModel() {
    val question = MutableStateFlow("")
    val sampleAnswer = MutableStateFlow("")
    val category = MutableStateFlow("situational")
    val difficulty = MutableStateFlow("medium")
    val tipsForAnswering = MutableStateFlow("")
    val yearAsked = MutableStateFlow(2023)
    val uiState = MutableStateFlow<UiState>(UiState.Idle)

    fun createInterview() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                repository.createInterview(
                    InterviewRequest(
                        question.value,
                        sampleAnswer.value,
                        category.value,
                        difficulty.value,
                        tipsForAnswering.value,
                        yearAsked.value
                    )
                )
                uiState.value = UiState.Success
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
} 
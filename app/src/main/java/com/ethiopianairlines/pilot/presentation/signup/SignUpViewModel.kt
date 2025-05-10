package com.ethiopianairlines.pilot.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    var name = MutableStateFlow("")
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    fun register() {
        _loading.value = true
        _error.value = null
        _success.value = false
        viewModelScope.launch {
            val result = registerUserUseCase(
                name.value,
                email.value,
                password.value
            )
            _loading.value = false
            result.onSuccess {
                _success.value = true
            }.onFailure {
                _error.value = it.message ?: "Registration failed"
            }
        }
    }
} 
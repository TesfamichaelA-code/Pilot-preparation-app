package com.ethiopianairlines.pilot.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun login() {
        _loading.value = true
        _error.value = null
        _success.value = false
        viewModelScope.launch {
            val result = loginUserUseCase(
                email.value,
                password.value
            )
            _loading.value = false
            result.onSuccess {
                _success.value = true
                _token.value = it.second
            }.onFailure {
                _error.value = it.message ?: "Login failed"
            }
        }
    }
} 
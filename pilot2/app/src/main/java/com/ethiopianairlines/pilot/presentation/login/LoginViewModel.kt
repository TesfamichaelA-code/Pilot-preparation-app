package com.ethiopianairlines.pilot.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.pilot.data.local.TokenDataStore
import com.ethiopianairlines.pilot.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        _loading.value = true
        _error.value = null
        _success.value = false
        _isAdmin.value = false
        
        viewModelScope.launch {
            val result = loginUserUseCase(
                email = email.value,
                password = password.value
            )
            
            _loading.value = false
            result.onSuccess { (user, token) ->
                // Save the token
                TokenDataStore.saveToken(getApplication(), token)
                _success.value = true
                _isAdmin.value = user.roles.contains("admin")
            }.onFailure {
                _error.value = it.message ?: "Login failed"
            }
        }
    }
} 
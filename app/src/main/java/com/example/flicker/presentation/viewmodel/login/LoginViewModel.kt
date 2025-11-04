package com.example.flicker.presentation.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// El sealed interface sigue siendo el mismo. Es perfecto para esto.
sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase // Koin proveerá esto
) : ViewModel() {
    // ... los StateFlows para username, password, y loginUiState son los mismos ...
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    fun setUsername(username:String) { this._username.value = username }
    fun setPassword(password:String) { this._password.value = password }

    fun loginWithEmail() {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                // Simplemente llamamos al caso de uso.
                // Si tiene éxito, no hace nada. Si falla, lanza una excepción.
                loginUseCase(email = _username.value, password = _password.value)
                _loginUiState.value = LoginUiState.Success
            } catch (e: Exception) {
                // Capturamos cualquier excepción del caso de uso (usuario no encontrado, contraseña incorrecta).
                _loginUiState.value = LoginUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _loginUiState.value = LoginUiState.Idle
    }
}

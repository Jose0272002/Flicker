package com.example.proyecto.presentation.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.User
import com.example.flicker.domain.usecase.users.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Definir un UiState para gestionar el estado del registro
sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

class RegisterViewModel(
    val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors

    // 2. Añadir el StateFlow para el estado del registro
    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    // ... (todas tus funciones 'set' como setUsername, setEmail, etc., permanecen igual) ...
    fun setUsername(username: String) { _user.value = _user.value.copy(username = username) }
    fun setName(name: String) { _user.value = _user.value.copy(name = name) }
    fun setPassword(password: String) { _user.value = _user.value.copy(password = password) }
    fun setPhone(phone: String) { _user.value = _user.value.copy(phone = phone) }
    fun setLastName(lastname: String) { _user.value = _user.value.copy(lastName = lastname) }
    fun setEmail(email: String) { _user.value = _user.value.copy(email = email) }
    fun setRole() { _user.value = _user.value.copy(role = "user") }

    private fun validateFields(): Boolean {
        val errors = mutableMapOf<String, String>()
        if (_user.value.username.isBlank()) { errors["username"] = "El nombre de usuario es requerido" }
        if (_user.value.email.isBlank()) { errors["email"] = "El email es requerido" }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_user.value.email).matches()) { errors["email"] = "El formato del email es inválido" }
        if (_user.value.password.isBlank()) { errors["password"] = "La contraseña es requerida" }
        else if (_user.value.password.length < 6) { errors["password"] = "La contraseña debe tener al menos 6 caracteres" }
        _validationErrors.value = errors
        return errors.isEmpty()
    }

    // 3. Modificar la función 'save' para usar el nuevo UiState y manejar errores
    fun save() {
        if (validateFields()) {
            viewModelScope.launch {
                _registerState.value = RegisterUiState.Loading
                try {
                    // Preparamos el usuario con su rol antes de enviarlo
                    val userToRegister = user.value.copy(role = "user")
                    registerUseCase(userToRegister)
                    _registerState.value = RegisterUiState.Success
                } catch (e: Exception) {
                    _registerState.value = RegisterUiState.Error(e.message ?: "Error desconocido al registrar")
                }
            }
        }
    }

    // 4. Añadir una función para resetear el estado después de un error o éxito
    fun resetRegisterState() {
        _registerState.value = RegisterUiState.Idle
    }
}

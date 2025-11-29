package com.example.flicker.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado para la UI, útil para mostrar feedback (cargando, error)
sealed interface ProfileUpdateState {
    object Idle : ProfileUpdateState
    object Loading : ProfileUpdateState
    object Success : ProfileUpdateState
    data class Error(val message: String) : ProfileUpdateState
}

class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _updateState = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Idle)
    val updateState = _updateState.asStateFlow()

    // Función para cambiar el avatar del usuario
    fun updateProfilePicture(drawableName: String) {
        val currentUser = sessionManager.getCurrentUserValue() ?: return

        viewModelScope.launch {
            _updateState.value = ProfileUpdateState.Loading
            try {
                // 1. Actualizar el campo en Firestore
                userRepository.updateUserPhoto(currentUser.id, drawableName)

                // 2. Actualizar el usuario en la sesión local para que la UI se refresque al instante
                sessionManager.updateUser(currentUser.copy(photoUrl = drawableName))

                _updateState.value = ProfileUpdateState.Success
            } catch (e: Exception) {
                _updateState.value = ProfileUpdateState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = ProfileUpdateState.Idle
    }
}
    
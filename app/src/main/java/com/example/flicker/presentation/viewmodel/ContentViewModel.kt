package com.example.flicker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class ContentViewModel : ViewModel() {

    // Inicializa el estado para que la pantalla completa por botón esté activada por defecto
    private val _isFullscreenByButton = MutableStateFlow(true) // ¡CAMBIO AQUÍ!
    val isFullscreenByButton: StateFlow<Boolean> = _isFullscreenByButton.asStateFlow()

    fun toggleFullscreen() {
        _isFullscreenByButton.value = !_isFullscreenByButton.value
    }

    fun setFullscreenState(isFullScreen: Boolean) {
        _isFullscreenByButton.value = isFullScreen
    }
}
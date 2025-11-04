package com.example.flicker.presentation.viewmodel.content

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContentViewModel : ViewModel() {

    // Inicializa el estado para que la pantalla completa por botón esté activada por defecto
    private val _isFullscreenByButton = MutableStateFlow(true) // ¡CAMBIO AQUÍ!
    val isFullscreenByButton: StateFlow<Boolean> = _isFullscreenByButton.asStateFlow()
    fun setFullscreenState(isFullScreen: Boolean) {
        _isFullscreenByButton.value = isFullScreen
    }
}
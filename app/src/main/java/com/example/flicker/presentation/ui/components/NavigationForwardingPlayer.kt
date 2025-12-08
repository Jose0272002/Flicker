package com.example.flicker.presentation.ui.components

import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

/**
 * Un ForwardingPlayer que intercepta los comandos de siguiente/anterior
 * para ejecutar una lógica de navegación personalizada en lugar del comportamiento por defecto.
 */
@UnstableApi
class NavigationForwardingPlayer(
    player: ExoPlayer,
    private val onNext: () -> Unit,
    private val onPrevious: () -> Unit
) : ForwardingPlayer(player) {

    override fun seekToNext() {
        onNext()
    }

    override fun seekToPrevious() {
        onPrevious()
    }

    override fun hasNextMediaItem(): Boolean {
        return true
    }

    override fun hasPreviousMediaItem(): Boolean {
        return true
    }
}

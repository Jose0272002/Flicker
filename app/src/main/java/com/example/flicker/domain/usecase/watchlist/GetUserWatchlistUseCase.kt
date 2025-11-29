package com.example.flicker.domain.usecase.watchlist

import com.example.flicker.domain.model.SessionManager
import com.example.flicker.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class GetUserWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository,
    private val sessionManager: SessionManager // <-- Inyecta SessionManager
) {
    operator fun invoke(): Flow<List<String>> {
        // Obtenemos el usuario directamente del SessionManager
        val user = sessionManager.getCurrentUserValue()
        return if (user != null) {
            watchlistRepository.observeUserWatchlist(user.id)
        } else {
            flow { emit(emptyList()) }
        }
    }
}
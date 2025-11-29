package com.example.flicker.domain.usecase.watchlist
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.domain.repository.WatchlistRepository

// ToggleWatchlistUseCase.kt
class ToggleWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository,
    private val sessionManager: SessionManager // <-- Inyecta SessionManager
) {
    suspend operator fun invoke(itemId: String, currentWatchlist: List<String>): Result<Unit> {
        // Obtenemos el usuario directamente del SessionManager
        val user = sessionManager.getCurrentUserValue() ?: return Result.failure(Exception("Usuario no autenticado"))

        return if (currentWatchlist.contains(itemId)) {
            watchlistRepository.removeFromWatchlist(user.id, itemId)
        } else {
            watchlistRepository.addToWatchlist(user.id, itemId)
        }
    }
}
package com.example.flicker.presentation.viewmodel.watchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.domain.usecase.movies.GetMoviesByIdsUseCase
import com.example.flicker.domain.usecase.watchlist.GetUserWatchlistUseCase
import com.example.flicker.domain.usecase.watchlist.ToggleWatchlistUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val sessionManager: SessionManager,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase,
    private val getUserWatchlistUseCase: GetUserWatchlistUseCase,
    private val getMoviesByIdsUseCase: GetMoviesByIdsUseCase // <-- 1. Inyecta el nuevo UseCase
) : ViewModel() {

    // Este StateFlow contiene los IDs. Lo usaremos para el toggle.
    val watchlistIds: StateFlow<List<String>> = sessionManager.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                getUserWatchlistUseCase()
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- 2. CREA UN NUEVO STATEFLOW PARA LOS OBJETOS MOVIE ---
    val watchlistMovies: StateFlow<List<Movie>> = watchlistIds
        .flatMapLatest { ids ->
            // Usamos el nuevo UseCase para obtener las películas
            getMoviesByIdsUseCase(ids)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    // 3. Actualiza el toggle para usar `watchlistIds`
    fun toggleWatchlist(itemId: String, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            toggleWatchlistUseCase(itemId, watchlistIds.value) // Usa la lista de IDs
                .onFailure { exception ->
                    Log.e("WatchlistVM", "Error toggling watchlist", exception)
                    onError(exception)
                }
        }
    }

    fun isWatchlisted(itemId: String): Boolean {
        return watchlistIds.value.contains(itemId) // Usa la lista de IDs
    }
}
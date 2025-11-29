package com.example.flicker.presentation.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.usecase.movies.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _movies = getMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults = searchText
        .combine(_movies) { text, movies ->
            if (text.isBlank()) {
                emptyList() // No mostrar nada si la búsqueda está vacía
            } else {
                movies.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // Función para comprobar si una película coincide con la búsqueda
    private fun Movie.doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            director,
            year.toString()
        ) + category

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

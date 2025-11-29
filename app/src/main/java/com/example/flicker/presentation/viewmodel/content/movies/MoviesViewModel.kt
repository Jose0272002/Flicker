package com.example.flicker.presentation.viewmodel.content.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.usecase.movies.GetMoviesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
): ViewModel() {
    private val _movies = getMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())
    val movies: StateFlow<List<Movie>> = _movies
}
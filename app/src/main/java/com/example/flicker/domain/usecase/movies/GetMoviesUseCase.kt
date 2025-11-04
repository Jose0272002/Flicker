package com.example.flicker.domain.usecase.movies

import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase (private val movieRepository: MovieRepository){
        operator fun invoke(): Flow<List<Movie>> {
            return movieRepository.list()
        }

}
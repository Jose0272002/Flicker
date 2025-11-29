package com.example.flicker.domain.usecase.movies

import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesByIdsUseCase(
    private val movieRepository: MovieRepository
) {
    // Este UseCase recibe una lista de IDs y devuelve un Flow con la lista de objetos Movie
    operator fun invoke(movieIds: List<String>): Flow<List<Movie>> {
        return movieRepository.getMoviesByIds(movieIds)
    }
}

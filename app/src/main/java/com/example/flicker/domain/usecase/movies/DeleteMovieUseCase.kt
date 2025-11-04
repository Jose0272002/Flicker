package com.example.flicker.domain.usecase.movies

import com.example.flicker.domain.repository.MovieRepository

class DeleteMovieUseCase(private val movieRepository: MovieRepository) {
        suspend operator fun invoke(id: String): Boolean{
            return movieRepository.delete(id)
    }
}
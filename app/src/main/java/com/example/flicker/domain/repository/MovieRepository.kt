package com.example.flicker.domain.repository

import com.example.flicker.domain.model.Movie
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getById(id: String): Movie?
    fun list(): Flow<List<Movie>>
    suspend fun save(movie: Movie): Boolean
    suspend fun delete(id: String): Boolean
    fun getMoviesByIds(movieIds: List<String>): Flow<List<Movie>>
}
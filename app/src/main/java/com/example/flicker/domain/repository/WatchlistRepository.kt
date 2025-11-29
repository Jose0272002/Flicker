package com.example.flicker.domain.repository

import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun addToWatchlist(userId: String, itemId: String): Result<Unit>
    suspend fun removeFromWatchlist(userId: String, itemId: String): Result<Unit>
    suspend fun getUserWatchlist(userId: String): Result<List<String>>
    fun observeUserWatchlist(userId: String): Flow<List<String>>
}

package com.example.flicker.domain.repository

import com.example.flicker.domain.model.Channel
import com.example.flicker.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun getById(id: String): Channel?
    suspend fun delete(id: String): Boolean
    fun list(): Flow<List<Channel>>
    suspend fun save(channel: Channel): Boolean
    fun getChannelsByIds(channelIds: List<String>): Flow<List<Channel>>
}
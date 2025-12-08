package com.example.flicker.domain.repository

import com.example.flicker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getById(id: String): User?
    fun list(): Flow<List<User>>
    suspend fun save(user: User): Boolean
    suspend fun delete(id: String): Boolean
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserByUserName(username: String): User?
    suspend fun updateUserPhoto(userId: String, photoUrl: String) // El nombre puede ser photoUrl o drawableName

}
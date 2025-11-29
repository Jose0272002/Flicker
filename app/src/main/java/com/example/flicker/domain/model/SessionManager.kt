package com.example.flicker.domain.model

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val KEY_LOGGED_IN_USER = "logged_in_user_key"

class SessionManager(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow() // Usar asStateFlow() para la vista externa

    init {
        // Al iniciar, intentamos cargar el usuario desde SharedPreferences
        val userJson = sharedPreferences.getString(KEY_LOGGED_IN_USER, null)
        if (userJson != null) {
            _currentUser.value = gson.fromJson(userJson, User::class.java)
        }
    }

    fun login(user: User) {
        _currentUser.value = user
        // Guardamos el usuario en SharedPreferences como un string JSON
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_LOGGED_IN_USER, userJson).apply()
    }

    fun logout() {
        _currentUser.value = null
        // Limpiamos la clave del usuario de SharedPreferences
        sharedPreferences.edit().remove(KEY_LOGGED_IN_USER).apply()
    }

    fun isLoggedIn(): Boolean = _currentUser.value != null

    fun getCurrentUserValue(): User? = _currentUser.value

    fun updateUser(updatedUser: User) {
        // Comprobamos que estamos actualizando el usuario correcto
        if (_currentUser.value?.id == updatedUser.id) {
            _currentUser.value = updatedUser
            // Actualizamos también la copia en SharedPreferences
            val userJson = gson.toJson(updatedUser)
            sharedPreferences.edit().putString(KEY_LOGGED_IN_USER, userJson).apply()
        }
    }
}

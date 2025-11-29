package com.example.flicker.data.repository

import android.util.Log
import com.example.flicker.domain.model.User
import com.example.flicker.domain.repository.WatchlistRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WatchlistFirestoreRepository(
    private val firestore:FirebaseFirestore
):WatchlistRepository{
    private val usersCollection = firestore.collection("Users")

    override suspend fun addToWatchlist(userId: String, itemId: String): Result<Unit> = try {
        usersCollection.document(userId).update("watchlist", FieldValue.arrayUnion(itemId)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun removeFromWatchlist(userId: String, itemId: String): Result<Unit> = try {
        usersCollection.document(userId).update("watchlist", FieldValue.arrayRemove(itemId)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserWatchlist(userId: String): Result<List<String>> = try {
        val user = usersCollection.document(userId).get().await().toObject(User::class.java)
        Result.success(user?.watchlist ?: emptyList())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeUserWatchlist(userId: String): Flow<List<String>> = callbackFlow {
        Log.d("RepoDebug", "observeUserWatchlist llamado con userId: '$userId'") // <-- AÑADIR LOG
        if (userId.isBlank()) {
            Log.w("RepoDebug", "userId está en blanco. No se observa nada.") // <-- AÑADIR LOG
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("RepoDebug", "Error en el listener de Firestore", error) // <-- AÑADIR LOG
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val watchlist = snapshot.get("watchlist") as? List<String> ?: emptyList()
                Log.i("RepoDebug", "Snapshot recibido. Watchlist tiene ${watchlist.size} items. Contenido: $watchlist") // <-- AÑADIR LOG
                trySend(watchlist)
            } else {
                Log.w("RepoDebug", "Snapshot nulo o el documento no existe para userId: $userId") // <-- AÑADIR LOG
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}
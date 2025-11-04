package com.example.flicker.data.repository

import com.example.flicker.domain.model.Movie
import com.example.flicker.domain.repository.MovieRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MovieFirestoreRepository (val firestore: FirebaseFirestore): MovieRepository {

        private val moviesCollection = firestore.collection("Movies")


        // Obtener una pelicula por ID
        override suspend fun getById(id: String): Movie? {
            return try {
                val documentSnapshot = moviesCollection.document(id).get().await()
                documentSnapshot.toObject(Movie::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun list(): Flow<List<Movie>> {
            // Esta implementación crea un Flow que actualiza la lista de peliculas
            // cada vez que hay un cambio en la base de datos
            return queryForList(
                moviesCollection,
                Movie::class.java
            )
        }

        // Agregar una nueva pelicula
        override suspend fun save(movie: Movie): Boolean {
            return try {
                moviesCollection.add(movie).await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Eliminar una pelicula por ID
        override suspend fun delete(id: String): Boolean {
            return try {
                moviesCollection.document(id).delete().await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Este método es siempre igual para cualquier repository
        private fun <T> queryForList(query: Query, clazz: Class<T>): Flow<List<T>> {
            return callbackFlow {

                val listener = query
                    .addSnapshotListener { snapshots, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }

                        val items = snapshots?.documents?.mapNotNull { doc ->
                            doc.toObject(clazz)

                        } ?: emptyList()

                        trySend(items)
                    }

                awaitClose() { listener.remove() }
            }
        }

        // Este método es siempre igual para cualquier repository
        private fun <T> queryForSingle(query: Query, clazz: Class<T>): Flow<T?> {
            return callbackFlow {
                val listener = query
                    .addSnapshotListener { snapshots, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }

                        val item = snapshots?.documents?.firstOrNull()?.toObject(clazz)

                        trySend(item)
                    }
                awaitClose() { listener.remove() }
            }
        }
}
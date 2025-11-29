package com.example.flicker.data.repository

import com.example.flicker.domain.model.Channel
import com.example.flicker.domain.repository.ChannelRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class ChannelFirestoreRepository (val firestore: FirebaseFirestore): ChannelRepository {

    private val channelsCollection = firestore.collection("Channels")


    // Obtener canal por ID
    override suspend fun getById(id: String): Channel? {
        return try {
            val documentSnapshot = channelsCollection.document(id).get().await()
            documentSnapshot.toObject(Channel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun list(): Flow<List<Channel>> {
        // Esta implementación crea un Flow que actualiza la lista de peliculas
        // cada vez que hay un cambio en la base de datos
        return queryForList(
            channelsCollection,
            Channel::class.java
        )
    }

    // Agregar una nueva pelicula
    override suspend fun save(channel: Channel): Boolean {
        return try {
            channelsCollection.add(channel).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Eliminar una pelicula por ID
    override suspend fun delete(id: String): Boolean {
        return try {
            channelsCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    override fun getChannelsByIds(channelIds: List<String>): Flow<List<Channel>> {
        if (channelIds.isEmpty()) {
            return flowOf(emptyList())
        }
        val query = channelsCollection.whereIn(FieldPath.documentId(), channelIds.take(30))

        return callbackFlow {
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val channels = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Channel::class.java)
                } ?: emptyList()
                trySend(channels)
            }
            awaitClose { listener.remove() }
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
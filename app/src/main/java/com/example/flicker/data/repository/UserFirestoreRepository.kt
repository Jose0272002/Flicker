package com.example.flicker.data.repository

import com.example.flicker.domain.model.User
import com.example.flicker.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(val firestore: FirebaseFirestore): UserRepository {
    private val usersCollection = firestore.collection("Users")


    // Obtener un usuario por ID
    override suspend fun getById(id: String): User? {
        return try {
            val documentSnapshot = usersCollection.document(id).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override suspend fun getUserByName(name: String): User? {
        // Ejecutamos una consulta en la colección 'users' donde el campo 'email' sea igual al proporcionado.
        val querySnapshot = firestore.collection("Users")
            .whereEqualTo("name", name)
            .limit(1) // Solo nos interesa el primer resultado
            .get()
            .await() // Esperamos el resultado de forma asíncrona

        // Si la consulta no devuelve documentos, retornamos null.
        if (querySnapshot.isEmpty) {
            return null
        }

        // Si hay un resultado, lo convertimos a nuestro objeto User y lo retornamos.
        return querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
    }
    override suspend fun getUserByEmail(email: String): User? {
        // Ejecutamos una consulta en la colección 'users' donde el campo 'email' sea igual al proporcionado.
        val querySnapshot = firestore.collection("Users")
            .whereEqualTo("name", email)
            .limit(1) // Solo nos interesa el primer resultado
            .get()
            .await() // Esperamos el resultado de forma asíncrona

        // Si la consulta no devuelve documentos, retornamos null.
        if (querySnapshot.isEmpty) {
            return null
        }

        // Si hay un resultado, lo convertimos a nuestro objeto User y lo retornamos.
        return querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
    }
    override fun list(): Flow<List<User>> {
        // Esta implementación crea un Flow que actualiza la lista de peliculas
        // cada vez que hay un cambio en la base de datos
        return queryForList(
            usersCollection,
            User::class.java
        )
    }

    // Agregar un usuario
    override suspend fun save(user: User): Boolean {
        return try {
            usersCollection.add(user).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Eliminar un usuario por ID
    override suspend fun delete(id: String): Boolean {
        return try {
            usersCollection.document(id).delete().await()
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
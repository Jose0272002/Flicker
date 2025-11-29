package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId
    val id : String,
    @JvmField
    val username: String,
    @JvmField
    val name: String?,
    @JvmField
    val lastName: String?,
    @JvmField
    val email: String,
    @JvmField
    val password: String,
    @JvmField
    val role: String,
    @JvmField
    val phone: String,
    @JvmField
    val watchlist: List<String> = emptyList(),
    @JvmField
    val photoUrl: String? = null
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        emptyList(),
        null
    )
}
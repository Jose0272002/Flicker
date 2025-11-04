package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId val id : String,
    val username: String,
    val name: String?,
    val surname: String?,
    val email: String,
    val password: String,
    val role: String,
    val phoneNumber: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}
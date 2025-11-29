package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Movie(
    @DocumentId @JvmField
    val id : String,
    @JvmField
    val name: String,
    @JvmField
    val director: String,
    @JvmField
    val category: List<String>,
    @JvmField
    val description: String,
    @JvmField
    val rating: Double?,
    @JvmField
    val image: String,
    @JvmField
    val year: Int,
    @JvmField
    val link: String
){
    constructor(): this(
        "",
        "",
        "",
        emptyList(),
        "",
        0.0,
        "",
        0,
        ""
    )
}
package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Movie(
    @DocumentId val id : String,
    val name: String,
    val category: String,
    val description: String,
    val rating: Double?,
    val image: String,
    val year: Int
){
    constructor(): this(
        "",
        "",
        "",
        "",
        0.0,
        "",
        0
    )
}
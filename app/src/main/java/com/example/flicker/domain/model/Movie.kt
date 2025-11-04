package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Movie(
    @DocumentId val id : String,
    val name: String,
    val category: List<String>,
    val description: String,
    val rating: Double?,
    val image: String,
    val year: Int,
    val link: String
){
    constructor(): this(
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
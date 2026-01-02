package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Channel(
    @DocumentId @JvmField
    val id : String,
    @JvmField
    val name: String,
    @JvmField
    val link: String,
    @JvmField
    val linkTV: List<String>,
    @JvmField
    val image: String
){
    constructor(): this(
        "",
        "",
        "",
        emptyList(),
        ""
    )
}
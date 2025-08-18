package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Channel(
    @DocumentId val id : String,
    val name: String,
    val link: String,
    val image: String
){
    constructor(): this(
        "",
        "",
        "",
        ""
    )
}
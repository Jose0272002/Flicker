package com.example.flicker.domain.model

import com.google.firebase.firestore.DocumentId

data class Movies(
    @DocumentId val id : String,
    val name: String,
    val category: String,
    val description: String,
    val rating: Double?,
){
    constructor(): this("","","","",0.0)
}
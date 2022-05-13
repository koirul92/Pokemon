package com.example.pokemon.ui.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id :Int?,
    val name : String,
    val image : String,
)
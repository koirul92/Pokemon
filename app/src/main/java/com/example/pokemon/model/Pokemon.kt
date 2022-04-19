package com.example.pokemon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon (
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("weight") val weight: Int,
    @Expose @SerializedName("height") val height: Int,
    @Expose @SerializedName("sprites") val sprites: Sprites,
    @Expose @SerializedName("stats") val stats:Stats
)

data class Sprites (
    @Expose @SerializedName("front_default") val frontDefault: String?,
    @Expose @SerializedName("front_shiny") val frontShiny: String?
)

data class Stats(
    @Expose @SerializedName("base_stat") val baseStat: Int,
    @Expose @SerializedName("effort") val effort: Int,
    @Expose @SerializedName("stat") val stat: Stat
)

data class Stat(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)
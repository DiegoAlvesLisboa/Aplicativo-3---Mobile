package com.example.grimoriohogwarts.model

data class CreatureListResponse(
    val data: List<Creature>?
)

data class Creature(
    val name: String?,
    val image: String?
)

package com.example.grimoriohogwarts.model

data class CharacterListResponse(
    val data: List<Character>?
)

data class Character(
    val name: String?,
    val species: String?,
    val house: String?
)

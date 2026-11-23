package com.example.grimoriohogwarts.network

import com.example.grimoriohogwarts.model.CharacterListResponse
import com.example.grimoriohogwarts.model.CreatureListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HogwartsApi {
    @GET("api/characters")
    suspend fun searchCharacters(@Query("search") name: String): CharacterListResponse

    @GET("api/creatures")
    suspend fun searchCreatures(@Query("search") name: String): CreatureListResponse
}

package com.example.myapplication

import com.example.myapplication.model.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("pokemon")
    fun getPosts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonListResponse> // Cambiato da Call<ApiClient> a Call<PokemonListResponse>
}
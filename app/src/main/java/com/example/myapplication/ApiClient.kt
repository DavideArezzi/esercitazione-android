package com.example.myapplication

import com.example.myapplication.model.PokemonListResponse
import com.example.myapplication.model.PostDetail
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiClient {
    @GET("pokemon")
    fun getPosts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonListResponse>

    @GET
    suspend fun getPostsDetails(@Url url: String): PostDetail
}


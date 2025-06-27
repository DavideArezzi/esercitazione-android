package com.example.myapplication.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Post>
)

data class Post(
    val name: String,
    val url: String
)

data class PostDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
)

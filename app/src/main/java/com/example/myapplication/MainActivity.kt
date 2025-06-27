package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Post
import com.example.myapplication.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var listPosts: List<Post>? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        val call: Call<PokemonListResponse> = api.getPosts(limit = 20, offset = 0)

        call.enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(
                call: Call<PokemonListResponse>,
                response: Response<PokemonListResponse>
            ) {
                if (response.isSuccessful) {
                    val pokemonApiResponse = response.body()
                    if (pokemonApiResponse != null) {
                        listPosts = pokemonApiResponse.results
                        Log.d("API_SUCCESS", "Posts fetched successfully: ${listPosts?.size} posts available.")

                        binding.recycleView.adapter = Adapter(listPosts ?: emptyList())
                        binding.recycleView.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                        Log.d("API_SUCCESS", "Adapter set with ${listPosts?.size ?: 0} posts.")
                    } else {
                        Log.e("API_ERROR", "Response successful but body is null. Code: ${response.code()}")
                    }
                } else {
                    Log.e("API_ERROR", "Error fetching posts: ${response.code()} - ${response.message()}")
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_ERROR", "Error body: $errorBody")
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "Exception while reading error body", e)
                    }
                }
            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Log.e("API_FAILURE", "API call failed: ${t.message}", t)
                // Considera di mostrare un messaggio all'utente qui
            }
        })
    }
}
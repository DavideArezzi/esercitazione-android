package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Per lifecycleScope
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.model.PostDetail // Importa il tuo modello PostDetail
import kotlinx.coroutines.launch // Per launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var apiClient: ApiClient // Definisci ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Non hai bisogno di enableEdgeToEdge qui a meno che non sia specificamente richiesto per il design
        // enableEdgeToEdge()
        // ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
        //     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //     insets
        // }

        // Inizializza Retrofit e ApiClient
        // È una buona pratica farlo tramite Dependency Injection o un Singleton,
        // ma per semplicità lo mettiamo qui.
        val retrofit = Retrofit.Builder()
            // Anche se usi @Url, Retrofit ha bisogno di una baseUrl valida per essere costruito.
            // Può essere la stessa della tua chiamata di lista o una qualsiasi URL valida se usi solo @Url.
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiClient = retrofit.create(ApiClient::class.java)

        // Ottieni l'URL passato dall'Intent
        val pokemonUrl = intent.getStringExtra("pokemon_url")

        if (pokemonUrl != null) {
            fetchPokemonDetails(pokemonUrl)
        } else {
            Log.e("DetailActivity", "Pokemon URL is null. Cannot fetch details.")
            binding.name.text = "Error: Pokémon data not found."
            // Potresti anche voler nascondere o mostrare altri messaggi di errore
            // binding.height.visibility = View.GONE
            // binding.weight.visibility = View.GONE
        }
    }

    private fun fetchPokemonDetails(url: String) {
        // Usa lifecycleScope per lanciare una coroutine.
        // La coroutine verrà automaticamente cancellata quando l'Activity viene distrutta.
        lifecycleScope.launch {
            try {
                // Fai la chiamata API (il metodo getPokemonDetail deve essere suspend)
                val pokemonDetail: PostDetail = apiClient.getPostsDetails(url)

                // Ora hai l'oggetto PostDetail, popola le tue View
                // Assicurati che il tuo layout activity_detail.xml abbia TextView con id: name, height, weight

                binding.name.text = pokemonDetail.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }

                // La PokeAPI restituisce l'altezza in decimetri e il peso in ettogrammi.
                // Convertili in metri e kg per la visualizzazione.
                binding.height.text = "Height: ${pokemonDetail.height / 10.0} m"
                binding.weight.text = "Weight: ${pokemonDetail.weight / 10.0} kg"

                // Se avessi un TextView per l'ID:
                // binding.idTextView.text = "ID: #${pokemonDetail.id}"

                // Qui potresti anche caricare un'immagine se il tuo PostDetail avesse un imageUrl
                // e tu avessi un ImageView (usando Glide o Coil).
                // Esempio:
                // if (pokemonDetail.imageUrl.isNotEmpty()) {
                //     Glide.with(this@DetailActivity).load(pokemonDetail.imageUrl).into(binding.pokemonImage)
                // }

                // Se PostDetail includesse liste (types, abilities, stats), le popoleresti qui.
                // Ad esempio, per una lista di stringhe 'types':
                // binding.typesTextView.text = "Types: ${pokemonDetail.types.joinToString(", ")}"

            } catch (e: Exception) {
                Log.e("DetailActivity", "Failed to fetch Pokémon details", e)
                binding.name.text = "Error loading details."
                // Gestisci l'errore, ad esempio mostrando un messaggio all'utente
                // binding.height.text = ""
                // binding.weight.text = ""
            }
        }
    }
}
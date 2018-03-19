package jalanechrissia.rivera.com.pokemonsearch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var pokeName: String? = null
    private var mPokeName: String? = null
    private val mStats = ArrayList<Stats>()
    private val mAbilities = ArrayList<Abilities>()
    private val mTypes = ArrayList<Types>()
    private val pokeSprites = "sprites"
    private val pokeKey = "front_default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerView2 = findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerView3 = findViewById<RecyclerView>(R.id.recyclerView3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener({
            pokeName = etPokeName.text.toString().toLowerCase()
            mPokeName = pokeName

            progressBar.visibility = View.VISIBLE
            textView2.text = "Please wait while we search for $mPokeName"

            val url = "https://pokeapi.co/api/v2/pokemon/$mPokeName/"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.e("Pokemon Search API", "Failed to fetch pokemon", e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val json = response.body()?.string()
                        displayResult(json)
                    }
                }
            })
        })
    }

    private fun displayResult(json: String?) {

        runOnUiThread {
            val gson = GsonBuilder().create()
            val pokemon = gson.fromJson(json, Pokemon::class.java)

            if (mPokeName == pokemon.name) {
                progressBar.visibility = View.GONE
                constraintLayout.visibility = View.VISIBLE
                textView2.setText("We found your pokemon!")
                val pokeImage = imgPokemon
                val jsonObject = JSONObject(json)
                val pokeSprites = jsonObject.getJSONObject(pokeSprites).getString(pokeKey)
                Picasso.with(this@MainActivity).load(pokeSprites).into(pokeImage)
                txtPokeName.text = "Name: " + pokemon.name.toUpperCase()
                txtPokeWeight.text = "Weight: " + pokemon.weight.toString()
                txtPokeHeight.text = "Height: " + pokemon.height.toString()

                mStats.clear()
                val statsLength = JSONObject(json).getJSONArray("stats").length()
                var statsCounter = 0
                for (i in 1..statsLength) {
                    var pokeStats = JSONObject(json).getJSONArray("stats").getJSONObject(statsCounter).getJSONObject("stat").getString("name")
                    var baseStats = JSONObject(json).getJSONArray("stats").getJSONObject(statsCounter).getString("base_stat").toString()
                    mStats.add(Stats(pokeStats, baseStats))
                    recyclerView.adapter = PokeAdapter(mStats)
                    statsCounter++
                }

                mAbilities.clear()
                val abilitiesLength = JSONObject(json).getJSONArray("abilities").length()
                var abilitiesCounter = 0
                for (i in 1..abilitiesLength) {
                    var pokemon_abilities = JSONObject(json).getJSONArray("abilities").getJSONObject(abilitiesCounter).getJSONObject("ability").getString("name")
                    mAbilities.add(Abilities(pokemon_abilities))
                    recyclerView2.adapter = AbilityAdapter(mAbilities)
                    abilitiesCounter++
                }

                mTypes.clear()
                val typesLength = JSONObject(json).getJSONArray("types").length()
                var typesCounter = 0
                for (i in 1..typesLength) {
                    var pokemon_types = JSONObject(json).getJSONArray("types").getJSONObject(typesCounter).getJSONObject("type").getString("name")
                    mTypes.add(Types(pokemon_types))
                    recyclerView3.adapter = TypeAdapter(mTypes)
                    typesCounter++
                }

            }
        }
    }
}

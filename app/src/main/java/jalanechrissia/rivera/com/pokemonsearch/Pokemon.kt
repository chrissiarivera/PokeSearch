package jalanechrissia.rivera.com.pokemonsearch

import com.google.gson.annotations.SerializedName

/**
 * Created by Jalane Chrissia on 19/03/2018.
 */
data class Pokemon(
        val name: String,
        val poke_img: Sprites,
        val weight: Int,
        val height: Int,
        val ability: String,
        val stat: String
)

data class Abilities(val abilities: String)
data class Stats(var stats: String, var base_stat: String)
data class Types(val types: String)
data class Sprites(@SerializedName("front_default") val frontDefault: String)
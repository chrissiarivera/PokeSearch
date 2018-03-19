package jalanechrissia.rivera.com.pokemonsearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pokemon_list.view.*

/**
 * Created by Jalane Chrissia on 19/03/2018.
 */
class AbilityAdapter (val abilityList: ArrayList<Abilities>): RecyclerView.Adapter<AbilityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.pokemon_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return abilityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val abilities: Abilities = abilityList[position]
        holder?.view?.txt1?.setText(abilities.abilities)
    }
    class ViewHolder (val view: View): RecyclerView.ViewHolder(view){

    }
}
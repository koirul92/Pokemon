package com.example.pokemon.ui.pokelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.databinding.PokemonItemBinding
import com.example.pokemon.model.PokeResult

class ListAdapter(private val pokemonClick:(Int)->Unit):RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root)

    var pokemonList: List<PokeResult> = emptyList<PokeResult>()

    fun setData(list: List<PokeResult>){
        pokemonList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(PokemonItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        val number = pokemon.url.replace("https://pokeapi.co/api/v2/pokemon/", "")
            .replace("/", "").toInt()
        val formattedNumber = number.toString().padStart(3, '0')
        val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
        Glide.with(holder.binding.root).load(imageUrl).into(holder.binding.ivPokemon)
        with(holder.binding){
            pokemonText.text="#${number} - ${pokemon.name}"
            item.setOnClickListener { pokemonClick(number) }
        }
    }
}
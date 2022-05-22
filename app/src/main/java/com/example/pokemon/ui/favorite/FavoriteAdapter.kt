package com.example.pokemon.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.databinding.PokemonItemBinding
import com.example.pokemon.local.Favorite

class FavoriteAdapter(private val onClick:(Favorite)->Unit) : ListAdapter<Favorite,FavoriteAdapter.ViewHolder>(FavoriteComparator()) {
    class ViewHolder(private val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(currentFavorite: Favorite,
                 onClick: (Favorite) -> Unit){

            binding.apply {
                val formattedNumber = currentFavorite.id.toString().padStart(3, '0')
                val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
                Glide.with(binding.ivPokemon)
                    .load(imageUrl)
                    .into(ivPokemon)
                root.setOnClickListener {
                    onClick(currentFavorite)
                }
                pokemonText.text = currentFavorite.name
            }

        }

    }

    class FavoriteComparator : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}
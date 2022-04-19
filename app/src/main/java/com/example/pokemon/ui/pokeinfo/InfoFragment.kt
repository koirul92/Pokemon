package com.example.pokemon.ui.pokeinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentInfoBinding
import com.example.pokemon.databinding.FragmentListBinding
import com.example.pokemon.ui.pokelist.ListViewModel


class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: InfoViewModel
    private val args:InfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        getInfoPokemon()
    }

    private fun getInfoPokemon() {
        /*val id = intent.extras?.get("id") as Int
        */
        val id = args.id
        viewModel.getPokemonInfo(id)
        viewModel.pokemonInfo.observe(viewLifecycleOwner, Observer { pokemon->
            val number = pokemon.id
            val formattedNumber = number.toString().padStart(3, '0')
            val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
            binding.apply {
                nameTextView.text = pokemon.name
                heightText.text = "Tinggi: ${pokemon.height/10.0}m"
                weightText.text = "Berat: ${pokemon.weight/10.0}kg"
                Glide.with(binding.root).load(imageUrl).into(imageView)
                btnBack.setOnClickListener {
                    val direct = InfoFragmentDirections.actionInfoFragmentToListFragment()
                    findNavController().navigate(direct)
                }
            }
        })
    }
}
package com.example.pokemon.ui.pokeinfo

import android.animation.ObjectAnimator
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
import java.util.*


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

        val id = args.id
        viewModel.getPokemonInfo(id)
        viewModel.pokemonInfo.observe(viewLifecycleOwner, Observer { pokemon->
            val number = pokemon.id
            val formattedNumber = number.toString().padStart(3, '0')
            val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
            binding.apply {
                nameTextView.text = pokemon.name
                heightText.text = "${pokemon.height/10.0}M"
                weightText.text = "${pokemon.weight/10.0}Kg"
                Glide.with(binding.root).load(imageUrl).into(imageView)
                btnBack.setOnClickListener {
                    val direct = InfoFragmentDirections.actionInfoFragmentToListFragment()
                    findNavController().navigate(direct)
                }
                tvType1.text = pokemon.types[0].type.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                if (pokemon.types.size > 1) {
                    tvType2.visibility = View.VISIBLE
                    tvType2.text = pokemon.types[1].type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
                } else {
                    tvType2.visibility = View.GONE
                }
                pbHp.max=100
                val currentProgressHp=pokemon.stats[0].baseStat
                ObjectAnimator.ofInt(pbHp,"progress",currentProgressHp).setDuration(2000).start()
                pbAtt.max=100
                val currentProgressAtt=pokemon.stats[1].baseStat
                ObjectAnimator.ofInt(pbAtt,"progress",currentProgressAtt).setDuration(2000).start()
                pbDef.max=100
                val currentProgressDef=pokemon.stats[2].baseStat
                ObjectAnimator.ofInt(pbDef,"progress",currentProgressDef).setDuration(2000).start()

                tvHp.text ="HP : ${currentProgressHp}"
                tvAtt.text ="ATT : ${currentProgressAtt}"
                tvDef.text ="DEF : ${currentProgressDef}"


            }
        })
    }
}
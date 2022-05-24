package com.example.pokemon.ui.pokeinfo

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentInfoBinding
import com.example.pokemon.local.Favorite
import com.example.pokemon.model.Status
import com.example.pokemon.ui.pokelist.ListAdapter
import com.example.pokemon.ui.pokelist.ListFragmentDirections
import com.example.pokemon.ui.viewmodel.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private val args:InfoFragmentArgs by navArgs()
    private val infoViewModel: InfoViewModel by viewModels()
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
        val id = args.id
        infoViewModel.getPokemonInfo(id)
        val progressDialog = ProgressDialog(requireActivity())
        infoViewModel.pokemonInfo.observe(viewLifecycleOwner){pokemon ->
            when (pokemon.status){
                Status.LOADING ->{
                    progressDialog.setMessage("Loading")
                    progressDialog.show()
                }
                Status.SUCCESS ->{
                    infoViewModel.pokemonInfo.observe(viewLifecycleOwner){
                        val number = pokemon.data?.id
                        val formattedNumber = number.toString().padStart(3, '0')
                        val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
                        binding.apply {
                            nameTextView.text = pokemon.data?.name
                            heightText.text = "${pokemon.data?.height?.div(10.0)}M"
                            weightText.text = "${pokemon.data?.weight?.div(10.0)}Kg"
                            Glide.with(binding.root).load(imageUrl).into(imageView)
                            btnBack.setOnClickListener {
                                findNavController().popBackStack()
                            }
                            tvType1.text = pokemon.data!!.types[0].type.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }

                            if (pokemon.data.types.size > 1) {
                                tvType2.visibility = View.VISIBLE
                                tvType2.text = pokemon.data.types[1].type.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                            } else {
                                tvType2.visibility = View.GONE
                            }
                            pbHp.max=100
                            val currentProgressHp=pokemon.data.stats[0].baseStat
                            ObjectAnimator.ofInt(pbHp,"progress",currentProgressHp).setDuration(2000).start()
                            pbAtt.max=100
                            val currentProgressAtt=pokemon.data.stats[1].baseStat
                            ObjectAnimator.ofInt(pbAtt,"progress",currentProgressAtt).setDuration(2000).start()
                            pbDef.max=100
                            val currentProgressDef=pokemon.data.stats[2].baseStat
                            ObjectAnimator.ofInt(pbDef,"progress",currentProgressDef).setDuration(2000).start()

                            tvHp.text ="HP : ${currentProgressHp}"
                            tvAtt.text ="ATT : ${currentProgressAtt}"
                            tvDef.text ="DEF : ${currentProgressDef}"
                        }
                        binding.ivFavorite.setOnClickListener {_ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                val isFavorite = infoViewModel.getFavorite(id)

                                activity?.runOnUiThread {
                                    if (isFavorite == null){
                                        val addFavorite = Favorite(
                                            id = pokemon.data?.id,
                                            name = pokemon.data!!.name,
                                            image = imageUrl
                                        )
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            infoViewModel.addFavorite(addFavorite)
                                        }
                                        infoViewModel.changeFavorite(true)
                                    }else{
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            infoViewModel.deleteFavorite(isFavorite)
                                        }
                                        infoViewModel.changeFavorite(false)
                                    }
                                }
                            }
                        }
                    }
                    infoViewModel.isFavorite.observe(viewLifecycleOwner){
                        if (it){
                            binding.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }else{
                            binding.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        val fav = infoViewModel.getFavorite(id)
                        if (fav == null){
                            infoViewModel.changeFavorite(false)
                        }else{
                            infoViewModel.changeFavorite(true)
                        }
                    }
                    progressDialog.dismiss()
                }
                Status.ERROR -> {
                    AlertDialog.Builder(requireContext()).setMessage("${pokemon.message}").show()
                    progressDialog.dismiss()
                }
            }

        }
    }
/*
    private fun getInfoPokemon() {

        val id = args.id
        infoViewModel.getPokemonInfo(id)
        infoViewModel.pokemonInfo.observe(viewLifecycleOwner, Observer { pokemon->
            val number = pokemon.data?.id
            val formattedNumber = number.toString().padStart(3, '0')
            val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
            binding.apply {
                nameTextView.text = pokemon.data?.name
                heightText.text = "${pokemon.data?.height?.div(10.0)}M"
                weightText.text = "${pokemon.data?.weight?.div(10.0)}Kg"
                Glide.with(binding.root).load(imageUrl).into(imageView)
                btnBack.setOnClickListener {
                    findNavController().popBackStack()
                }
                tvType1.text = pokemon.data?.types[0].type.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                if (pokemon.data.types.size > 1) {
                    tvType2.visibility = View.VISIBLE
                    tvType2.text = pokemon.data.types[1].type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
                } else {
                    tvType2.visibility = View.GONE
                }
                pbHp.max=100
                val currentProgressHp=pokemon.data.stats[0].baseStat
                ObjectAnimator.ofInt(pbHp,"progress",currentProgressHp).setDuration(2000).start()
                pbAtt.max=100
                val currentProgressAtt=pokemon.data.stats[1].baseStat
                ObjectAnimator.ofInt(pbAtt,"progress",currentProgressAtt).setDuration(2000).start()
                pbDef.max=100
                val currentProgressDef=pokemon.data.stats[2].baseStat
                ObjectAnimator.ofInt(pbDef,"progress",currentProgressDef).setDuration(2000).start()

                tvHp.text ="HP : ${currentProgressHp}"
                tvAtt.text ="ATT : ${currentProgressAtt}"
                tvDef.text ="DEF : ${currentProgressDef}"


            }
            // Favorite
            binding.ivFavorite.setOnClickListener {_ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val isFavorite = infoViewModel.getFavorite(id)

                    activity?.runOnUiThread {
                        if (isFavorite == null){
                            val addFavorite = Favorite(
                                id = pokemon.data?.id,
                                name = pokemon.data!!.name,
                                image = imageUrl
                            )
                            lifecycleScope.launch(Dispatchers.IO) {
                                infoViewModel.addFavorite(addFavorite)
                            }
                            infoViewModel.changeFavorite(true)
                        }else{
                            lifecycleScope.launch(Dispatchers.IO) {
                                infoViewModel.deleteFavorite(isFavorite)
                            }
                            infoViewModel.changeFavorite(false)
                        }
                    }
                }
            }
        })
        infoViewModel.isFavorite.observe(viewLifecycleOwner){
            if (it){
                binding.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                binding.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val fav = infoViewModel.getFavorite(id)
            if (fav == null){
                infoViewModel.changeFavorite(false)
            }else{
                infoViewModel.changeFavorite(true)
            }
        }
    }*/
}
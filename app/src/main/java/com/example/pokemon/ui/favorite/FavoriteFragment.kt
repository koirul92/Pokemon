package com.example.pokemon.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentFavoriteBinding
import com.example.pokemon.local.Favorite
import com.example.pokemon.ui.viewmodel.FavoriteViewModel
import com.example.pokemon.ui.viewmodel.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.allFavorites.observe(viewLifecycleOwner){
            //show adapter
            showFavoritePokemon(it)
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_listFragment)
        }

        favoriteViewModel.getAllFavorites()

    }

    private fun showFavoritePokemon(list: List<Favorite?>?) {
        val adapter= FavoriteAdapter {
            val action = FavoriteFragmentDirections
                .actionFavoriteFragmentToInfoFragment(it.id!!)
            findNavController().navigate(action)
        }
        adapter.submitList(list)
        binding?.rvFavorite?.adapter = adapter
    }
}
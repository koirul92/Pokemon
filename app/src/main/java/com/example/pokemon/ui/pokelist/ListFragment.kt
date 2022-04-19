package com.example.pokemon.ui.pokelist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.MainActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentListBinding
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.service.PokeApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)


        getAllPokemon()

        binding.tvWelcome.text = "Welcome ${sharedPreference?.getString("username",null)}"

        binding.tvLogout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreference.edit()
            editor.clear()
            editor.apply()
            val direct = ListFragmentDirections.actionListFragmentToSplashFragment()
            findNavController().navigate(direct)
        }
    }
    private fun getAllPokemon(){
        /*binding.rvPokemon.layoutManager = LinearLayoutManager(requireContext())*/


        viewModel.getPokemonList()

        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            (binding.rvPokemon.adapter as ListAdapter).setData(list)

        })
        binding.rvPokemon.adapter = ListAdapter{
            val id = it
            val direction = ListFragmentDirections.actionListFragmentToInfoFragment(id)
            findNavController().navigate(direction)
    }

    }
}
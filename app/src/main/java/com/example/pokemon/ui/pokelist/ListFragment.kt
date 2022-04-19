package com.example.pokemon.ui.pokelist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentListBinding

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
        getAllPokemon()
    }
    private fun getAllPokemon(){
        /*binding.rvPokemon.layoutManager = LinearLayoutManager(requireContext())*/
        binding.rvPokemon.adapter = ListAdapter{
            /*val intent = Intent(this, PokeInfoActivity::class.java)
            intent.putExtra("id", it)
            startActivity(intent)*/
            /*val id = it
            val direction = ListFragmentDirections.actionListFragment2ToInfoFragment(id)
            findNavController().navigate(direction)*/
        }

        viewModel.getPokemonList()

        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            (binding.rvPokemon.adapter as ListAdapter).setData(list)
        })
    }
}
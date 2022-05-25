package com.example.pokemon.ui.pokelist

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pokemon.R
import com.example.pokemon.databinding.CustomDialogBinding
import com.example.pokemon.databinding.FragmentListBinding
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.model.Status
import com.example.pokemon.ui.viewmodel.ListViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDataUser()
        viewModel.userSession.observe(viewLifecycleOwner){
            binding.tvWelcome.text = it.name
        }
        binding.btnFav.setOnClickListener {
            it.findNavController().navigate(R.id.action_listFragment_to_favoriteFragment)
        }

        viewModel.getPokemonList()
        val progressDialog = ProgressDialog(requireActivity())
        viewModel.pokemonList.observe(viewLifecycleOwner){
                list->
            when (list.status){
                Status.LOADING ->{
                    progressDialog.setMessage("Loading")
                    progressDialog.show()
                }
                Status.SUCCESS ->{
                    viewModel.pokemonList.observe(viewLifecycleOwner, Observer {
                        (binding.rvPokemon.adapter as ListAdapter).setData(list.data!!.results)
                    })
                    binding.rvPokemon.adapter = ListAdapter {
                        val id = it
                        val direction = ListFragmentDirections.actionListFragmentToInfoFragment(id)
                        findNavController().navigate(direction)
                    }
                    progressDialog.dismiss()
                }
                Status.ERROR -> {
                    AlertDialog.Builder(requireContext()).setMessage("${list.message}").show()
                    progressDialog.dismiss()
                }
            }
        }


        binding.tvWelcome.setOnClickListener {
            val direct = ListFragmentDirections.actionListFragmentToProfileFragment()
            findNavController().navigate(direct)
        }

        binding.tvLogout.setOnClickListener {
            viewModel.deleteDataUser()
            viewModel.userSession.observe(viewLifecycleOwner){
                if (it.id == -1 &&findNavController().currentDestination?.id==R.id.listFragment){
                    val direct = ListFragmentDirections.actionListFragmentToSplashFragment()
                    findNavController().navigate(direct)
                }
            }
        }
    }

}
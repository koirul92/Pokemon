package com.example.pokemon.ui.pokelist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.MainActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.CustomDialogBinding
import com.example.pokemon.databinding.FragmentListBinding
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.service.PokeApiClient
import com.example.pokemon.ui.room.User
import com.example.pokemon.ui.room.UserDatabase
import com.example.pokemon.ui.room.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    lateinit var repo: UserRepository
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
        repo = UserRepository(requireContext())
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        getUser()
        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        repo = UserRepository(requireContext())
        getAllPokemon()
        viewModel.userLoggedin.observe(viewLifecycleOwner){
            binding.tvWelcome.text = "Welcome Tamer ${it.name}"
        }
        binding.tvLogout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreference.edit()
            editor.clear()
            editor.apply()
            val direct = ListFragmentDirections.actionListFragmentToSplashFragment()
            findNavController().navigate(direct)
        }
    }
    private fun getAllPokemon(){
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

    fun getUser() {
        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreference.getString("username",null).toString()
        val password = sharedPreference.getString("password",null).toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val data = repo.getUser(username, password)
            runBlocking(Dispatchers.Main) {
                if (data != null) {
                    val user = User(
                        data.id,
                        data.name,
                        data.email,
                        data.password
                    )
                    viewModel.getUser(user)
                    viewModel.userLoggedin.observe(viewLifecycleOwner, Observer {
                        binding.tvWelcome.setOnClickListener {
                            val dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            dialog.setCancelable(false)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            viewModel.userLoggedin.observe(viewLifecycleOwner){
                                dialogBinding.etUsername.setText("${it.name}")
                            }
                            viewModel.userLoggedin.observe(viewLifecycleOwner){
                                dialogBinding.etEmail.setText("${it.email}")
                            }
                            viewModel.userLoggedin.observe(viewLifecycleOwner){
                                dialogBinding.etPassowrd.setText("${it.password}")
                            }

                            dialogBinding.btnUpdate.setOnClickListener {
                                lifecycleScope.launch(Dispatchers.IO){
                                    val id = user.id
                                    val usernameU = dialogBinding.etUsername.text.toString()
                                    val emailU = dialogBinding.etEmail.text.toString()
                                    val passwordU = dialogBinding.etPassowrd.text.toString()
                                    val data2 = User(id,usernameU,emailU,passwordU)

                                    val result = repo.updateItem(data2)
                                    runBlocking(Dispatchers.Main){
                                        if (result != 0){
                                            val editor = sharedPreference.edit()
                                            editor.putString("username",usernameU)
                                            editor.putString("password",passwordU)
                                            editor.apply()
                                            Toast.makeText(
                                                requireContext(),
                                                "Data ${data2.name} Berhasil Di Update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.getUser(data2)
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Data ${data2.name} Gagal Di update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.getUser(data2)
                                            dialog.dismiss()
                                        }
                                    }
                                }
                            }
                            dialog.show()
                        }
                    })
                }
            }
        }
    }
}
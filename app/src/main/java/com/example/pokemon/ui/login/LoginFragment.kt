package com.example.pokemon.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokemon.MainActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentLoginBinding
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.ui.pokelist.ListViewModel
import com.example.pokemon.ui.pokelist.ViewModelFactory
import com.example.pokemon.ui.room.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    lateinit var repo:UserRepository
    private lateinit var viewModel: ListViewModel
    lateinit var dataStore:DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(),ViewModelFactory(dataStore))[ListViewModel::class.java]
        repo = UserRepository(requireContext())
        userLogin()
        binding.btnLogin.setOnClickListener {
            val username = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO){
                val Login = repo.login(username,password)
                activity?.runOnUiThread {
                    if (Login ==null){
                        Snackbar.make(it,"Username atau Password anda salah", Snackbar.LENGTH_LONG).show()
                    }else{
                        val direct = LoginFragmentDirections.actionLoginFragmentToListFragment(Login)
                        findNavController().navigate(direct)
                    }
                }
                if (Login != null){
                    viewModel.setDataUser(Login)
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            val direct = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(direct)
        }
    }

    fun userLogin(){
        viewModel.apply {
            getDataUser().observe(viewLifecycleOwner){
                if (it.id != DataStoreManager.DEFAULT_ID){
                    findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
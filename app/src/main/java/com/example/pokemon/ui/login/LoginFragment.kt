package com.example.pokemon.ui.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.pokemon.databinding.FragmentLoginBinding
import com.example.pokemon.ui.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //userLogin()
        viewModel.login.observe(viewLifecycleOwner, Observer { user->
            if(user != null){
                viewModel.setDataUser(user)
                Toast.makeText(requireContext(),"Login",Toast.LENGTH_SHORT).show()
                val direct = LoginFragmentDirections.actionLoginFragmentToListFragment()
                findNavController().navigate(direct)
                //viewModel.login.removeObservers(viewLifecycleOwner)
            }else{
                val snackbar = Snackbar.make(requireContext(),binding.root,"Gagal Login",Snackbar.LENGTH_SHORT)
                snackbar.setAction("OK"){snackbar.dismiss()}
                snackbar.show()
                //viewModel.login.removeObservers(viewLifecycleOwner)
            }
        })
        binding.btnLogin.setOnClickListener {
            val username = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                username.isNullOrEmpty() -> {
                    binding.materialEmail.error = "Kolom nama harus diisi"
                }
                password.isNullOrEmpty() -> {
                    binding.materialPassword.error = "Kolom password harus diisi"
                }else ->{
                viewModel.login(username,password)
            }
            }
        }
        binding.btnRegister.setOnClickListener {
            val direct = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(direct)
        }
    }

    /*fun userLogin(){
        viewModel.apply {
            getDataUser().observe(viewLifecycleOwner){
                if (it.id != DataStoreManager.DEFAULT_ID){
                    findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                }
            }
        }
    }*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
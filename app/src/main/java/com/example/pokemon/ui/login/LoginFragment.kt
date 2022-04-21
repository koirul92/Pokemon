package com.example.pokemon.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokemon.MainActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentLoginBinding
import com.example.pokemon.ui.room.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    lateinit var repo:UserRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = UserRepository(requireContext())
        val sharedPreferences = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        binding.btnLogin.setOnClickListener {
            val username = binding.etUser.text.toString()
            val password = binding.etPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO){
                val Login = repo.login(username,password)
                activity?.runOnUiThread {
                    if (Login ==null){
                        Snackbar.make(it,"Username atau Password anda salah", Snackbar.LENGTH_LONG).show()
                    }else{
                        val editor = sharedPreferences.edit()
                        editor.putString("username",username)
                        editor.putString("password",password)
                        editor.apply()
                        val direct = LoginFragmentDirections.actionLoginFragmentToListFragment()
                        findNavController().navigate(direct)
                    }
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            val direct = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(direct)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
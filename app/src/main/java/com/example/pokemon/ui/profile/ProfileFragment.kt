package com.example.pokemon.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentProfileBinding
import com.example.pokemon.local.User
import com.example.pokemon.ui.register.RegisterFragmentDirections
import com.example.pokemon.ui.viewmodel.AuthViewModel
import com.example.pokemon.ui.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserFromPref()
        viewModel.userSession.observe(viewLifecycleOwner){
            val id = it.id
            binding.etUsername.setText("${it.name}")
            binding.etEmail.setText("${it.email}")
            binding.etPassowrd.setText("${it.password}")
            binding.btnUpdate.setOnClickListener {
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassowrd.text.toString()
                val data = User(id, username, email, password,"")
                viewModel.updateUser(data)
                viewModel.setDataUser(data)
                viewModel.update.observe(viewLifecycleOwner){int->
                    if(int == null){
                        Toast.makeText(requireContext(),"Gagal Update", Toast.LENGTH_SHORT).show()
                    }else{
                        val direct = ProfileFragmentDirections.actionProfileFragmentToListFragment()
                        findNavController().navigate(direct)
                    }
                }
            }
        }
    }
}
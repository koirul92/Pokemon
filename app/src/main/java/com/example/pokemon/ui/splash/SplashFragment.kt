package com.example.pokemon.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getUserFromPref()
            viewModel.apply {
                userSession.observe(viewLifecycleOwner){
                    /*if (it.id != DataStoreManager.DEFAULT_ID){
                        findNavController().navigate(R.id.action_splashFragment_to_listFragment)
                    }else{
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }*/
                }
            }
        },1000)
    }
}
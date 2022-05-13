package com.example.pokemon.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pokemon.MainActivity.Companion.SHARED_PREFERENCES
import com.example.pokemon.R
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.ui.pokelist.ListViewModel
import com.example.pokemon.ui.pokelist.ViewModelFactory

class SplashFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    lateinit var dataStore:DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(dataStore))[ListViewModel::class.java]
        val sharedPreference = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val sharedPreferences = sharedPreference?.getString("username","")

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.apply {
                getDataUser().observe(viewLifecycleOwner){
                    if (it.id != DataStoreManager.DEFAULT_ID){
                        findNavController().navigate(R.id.action_splashFragment_to_listFragment)
                    }else{
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }
        },5000)
    }
}
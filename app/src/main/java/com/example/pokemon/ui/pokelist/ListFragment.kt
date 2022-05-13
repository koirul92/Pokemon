package com.example.pokemon.ui.pokelist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon.MainActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.CustomDialogBinding
import com.example.pokemon.databinding.FragmentListBinding
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.service.PokeApiClient
import com.example.pokemon.ui.room.User
import com.example.pokemon.ui.room.UserDatabase
import com.example.pokemon.ui.room.repository.UserRepository
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    lateinit var repo: UserRepository
    private lateinit var viewModel: ListViewModel
    private lateinit var dataStore: DataStoreManager
    private val args:ListFragmentArgs by navArgs()
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
        repo = UserRepository(requireContext())
        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]
        dataStore = DataStoreManager(requireActivity())
        getUser()
        repo = UserRepository(requireContext())
        getAllPokemon()
        binding.btnFav.setOnClickListener {
            it.findNavController().navigate(R.id.action_listFragment_to_favoriteFragment)
        }
        viewModel.getDataUser().observe(viewLifecycleOwner) {
            binding.tvWelcome.text = "Welcome Tamer ${it.name}"
        }
        binding.logo.setOnClickListener {
            openImagePicker()
        }
        binding.tvLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dataStore.deleteUserFromPref()
            }
            val direct = ListFragmentDirections.actionListFragmentToSplashFragment()
            findNavController().navigate(direct)
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data
                    fileUri?.let { loadImage(it) }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
    private fun loadImage(uri: Uri) {
        uri?.let {

        }
    }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()
            .saveDir(
                File(
                    requireContext().externalCacheDir,
                    "ImagePicker"
                )
            ) //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun getAllPokemon() {
        viewModel.getPokemonList()
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            (binding.rvPokemon.adapter as ListAdapter).setData(list)
        })
        binding.rvPokemon.adapter = ListAdapter {
            val id = it
            val direction = ListFragmentDirections.actionListFragmentToInfoFragment(id)
            findNavController().navigate(direction)
        }

    }
//NB Dialog fragment custom dialog haram
    fun getUser() {
        viewModel.getDataUser().observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                //run blocking haram
                runBlocking(Dispatchers.Main) {
                    viewModel.getDataUser().observe(viewLifecycleOwner, Observer {
                        val id = it.id
                        binding.tvWelcome.setOnClickListener {
                            val dialogBinding =
                                CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder =
                                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            dialog.setCancelable(false)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            viewModel.getDataUser().observe(viewLifecycleOwner) {
                                dialogBinding.etUsername.setText("${it.name}")
                            }
                            viewModel.getDataUser().observe(viewLifecycleOwner) {
                                dialogBinding.etEmail.setText("${it.email}")
                            }
                            viewModel.getDataUser().observe(viewLifecycleOwner) {
                                dialogBinding.etPassowrd.setText("${it.password}")
                            }
                            dialogBinding.ivUser.setOnClickListener {
                                openImagePicker()

                            }

                            dialogBinding.btnUpdate.setOnClickListener {
                                val usernameU = dialogBinding.etUsername.text.toString()
                                val emailU = dialogBinding.etEmail.text.toString()
                                val passwordU = dialogBinding.etPassowrd.text.toString()
                                val data2 = User(id, usernameU, emailU, passwordU,"")

                                lifecycleScope.launch(Dispatchers.IO) {
                                    val result = repo.updateItem(data2)
                                    runBlocking(Dispatchers.Main) {
                                        if (result != 0) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Data ${data2.name} Berhasil Di Update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.setDataUser(data2)
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Data ${data2.name} Gagal Di update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
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

    private fun getRequiredPermission(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }


}
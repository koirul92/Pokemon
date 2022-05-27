package com.example.pokemon.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentProfileBinding
import com.example.pokemon.local.User
import com.example.pokemon.ui.register.RegisterFragmentDirections
import com.example.pokemon.ui.viewmodel.AuthViewModel
import com.example.pokemon.ui.viewmodel.ListViewModel
import com.example.pokemon.utils.PermissionUtils
import com.example.pokemon.utils.StorageUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private var imageUri: Uri? = null
    //    private var imageUriToUpdate: Uri? = null
    private var imageSource = -1

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

        binding.ivUser.setOnClickListener {
            if (PermissionUtils.isPermissionsGranted(requireActivity(), getRequiredPermission()) {
                    activity?.let {
                        requestPermissionLauncher.launch(getRequiredPermission())
                        imageSource=2
                    }
                }){
                openCamera()
            }
        }

        viewModel.getUserFromPref()

        viewModel.update.observe(viewLifecycleOwner){int->
            if(int == null){
                Toast.makeText(requireContext(),"Gagal Update", Toast.LENGTH_SHORT).show()
            }else{
                val direct = ProfileFragmentDirections.actionProfileFragmentToListFragment()
                findNavController().navigate(direct)
            }
        }
        viewModel.userSession.observe(viewLifecycleOwner){
            val id = it.id
            binding.etUsername.setText("${it.name}")
            binding.etEmail.setText("${it.email}")
            binding.etPassowrd.setText("${it.password}")
            if (it?.image!=""){
                if (imageUri!=null){
                    binding.ivUser.setImageURI(imageUri)
                }else{
                    binding.ivUser.setImageURI(it?.image?.toUri())
                }
            }
            binding.btnUpdate.setOnClickListener {
                val image = if (imageUri==null){
                    ""
                }else{
                    imageUri.toString()
                }
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassowrd.text.toString()
                val data = User(id, username, email, password,image)
                viewModel.updateUser(data)
                viewModel.setDataUser(data)
            }
        }
    }

    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            imageUri?.let { loadImage(it) }
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                val uri = StorageUtils.savePhotoToExternalStorage(
                    context?.contentResolver,
                    UUID.randomUUID().toString(),
                    bitmap
                )
                imageUri = uri
                uri?.let {
                    loadImage(it)
                }
            }
        }

    private fun loadImage(uri: Uri) {
        binding.ivUser.setImageURI(uri)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        galleryLauncher.launch(intentGallery)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
        if (granted) {
            openImage(imageSource)
        }
    }

    private fun openImage(imageSource: Int) {
        if (imageSource==1){
            openGallery()
        }else{
            openCamera()
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
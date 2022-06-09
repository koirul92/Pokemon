package com.example.pokemon.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.local.User
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.Roboto
import com.example.pokemon.ui.theme.pokemom2
import com.example.pokemon.ui.viewmodel.AuthViewModel
import com.example.pokemon.ui.viewmodel.ListViewModel
import com.example.pokemon.utils.PermissionUtils
import com.example.pokemon.utils.StorageUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {
 /*   private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!*/
    private val viewModel: AuthViewModel by viewModels()
    private var imageUri: Uri? = null
    //    private var imageUriToUpdate: Uri? = null
    private var imageSource = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
   /*     _binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root*/
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                PokemonTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column {
                            Header()
                            Update()
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun Header() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Profile",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_supervised_user_circle_24),
                contentDescription = "image app",
                modifier = Modifier.size(128.dp)
            )
        }
    }

    @Composable
    fun Update() {
        viewModel.getUserFromPref()
        var username by remember { mutableStateOf("${viewModel.userSession.value?.name}") }
        var email by remember { mutableStateOf("${viewModel.userSession.value?.email}") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        var confirmPasswordVisibility by remember { mutableStateOf(false) }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
        Spacer(
            modifier = Modifier
                .height(32.dp)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(text = "Username") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text(text = "Confirm Password") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                ),
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(
                            imageVector = if (confirmPasswordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (username == "" || email == "" || password == "" || confirmPassword == "") {
                            AlertDialog.Builder(requireContext())
                                .setTitle("")
                                .setMessage("Semua kolom harus diisi")
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(
                                requireContext(),
                                "Password konfirmasi tidak sama",
                                Toast.LENGTH_LONG
                            ).show()
                            confirmPassword = ""
                        } else {
                            val user = User(viewModel.userSession.value?.id, username, email, password,"")
                            viewModel.updateUser(user)
                            viewModel.setDataUser(user)
                            viewModel.update.observe(viewLifecycleOwner){int->
                                if(int == null){
                                    Toast.makeText(requireContext(),"Gagal Update", Toast.LENGTH_SHORT).show()
                                }else{
                                    val direct = ProfileFragmentDirections.actionProfileFragmentToListFragment()
                                    findNavController().navigate(direct)
                                }
                            }

                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = pokemom2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Update",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PokemonTheme {
            Column {
                Header()
                Update()
            }
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }*/
}
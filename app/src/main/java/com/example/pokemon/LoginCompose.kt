package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.pokemon.ui.theme.*
import com.example.pokemon.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.http2.Header

@AndroidEntryPoint
class LoginCompose : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Header()
                        Login()
                    }
                }
            }
        }
    }

    @Composable
    fun Header(){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Login",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    @Composable
    fun Login() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        Spacer(
            modifier = Modifier
                .height(128.dp)
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
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
                    if (username == "" || password == "") {
                        android.app.AlertDialog.Builder(this@LoginCompose)
                            .setTitle("")
                            .setMessage("Username atau Password tidak boleh kosong")
                            .setPositiveButton("Coba login kembali") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }else{
                        viewModel.login(username, password)
                        viewModel.login.observe(this@LoginCompose) { user ->
                            if (user == null) {
                                Toast.makeText(
                                    this@LoginCompose,
                                    "Login Gagal",
                                    Toast.LENGTH_LONG
                                ).show()
//                            android.app.AlertDialog.Builder(requireContext())
//                                .setTitle("")
//                                .setMessage("Gagal login mungkin anda salah memasukkan username atau password.")
//                                .setPositiveButton("Coba login kembali"){dialog,_ ->
//                                    dialog.dismiss()
//                                }
//                                .show()
                            } else {
                                Toast.makeText(this@LoginCompose,"Selamat datang ${user.name}",Toast.LENGTH_LONG).show()
                                viewModel.setDataUser(user)
                                startActivity(Intent(this@LoginCompose, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = pokemom2),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sign In",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.clickable(
                    onClick = {startActivity(Intent(this@LoginCompose, RegisterCompose::class.java))
                        finish()}),
                text = "Don't Have an Account? Register",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun DefaultPreview() {
        PokemonTheme {
            Column {
                Header()
                Login()
            }
        }
    }
}

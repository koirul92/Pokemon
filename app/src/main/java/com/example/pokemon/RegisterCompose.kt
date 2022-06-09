package com.example.pokemon

import android.app.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.pokemon.local.User
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.Roboto
import com.example.pokemon.ui.theme.RobotoCondensed
import com.example.pokemon.ui.theme.pokemom2
import com.example.pokemon.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterCompose : ComponentActivity() {
    private val viewModel:AuthViewModel by viewModels()
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
                        Register()
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
                text = "Sign Up",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    @Composable
    fun Register() {
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        var confirmPasswordVisibility by remember { mutableStateOf(false) }
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
                    fontFamily =Roboto,
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
                            AlertDialog.Builder(this@RegisterCompose)
                                .setTitle("")
                                .setMessage("Semua kolom harus diisi")
                                .setPositiveButton("Coba login kembali") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(
                                this@RegisterCompose,
                                "Password konfirmasi tidak sama",
                                Toast.LENGTH_LONG
                            ).show()
                            confirmPassword = ""
                        } else {
                            val user = User(null, username, email, password,"")
                            viewModel.register(user)
                            viewModel.register.observe(this@RegisterCompose) {
                                if (it != null) {
                                    if (it != 0.toLong()) {
                                        Toast.makeText(
                                            this@RegisterCompose,
                                            "Registration success",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        startActivity(Intent(this@RegisterCompose, LoginCompose::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@RegisterCompose,
                                            "Registration failed",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = pokemom2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sign Up",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                /*Text(
                    modifier = Modifier.clickable(
                        onClick = {
                            startActivity(Intent(this@RegisterCompose, LoginCompose::class.java))
                            finish()
                        }),
                    text = "Sign In",
                    style = TextStyle(
                        color = pokemom2,
                        fontSize = 18.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium
                    )
                )*/
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PokemonTheme {
            Column {
                Header()
                Register()
            }
        }
    }
}


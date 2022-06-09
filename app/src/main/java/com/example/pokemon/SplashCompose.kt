package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.airbnb.lottie.compose.*
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashCompose : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        SplashScreen()
                    }
                }
            }
        }
    }
    @Composable
    fun SplashScreen() {
        LaunchedEffect(key1 = true) {
            delay(5000)
            viewModel.getUserFromPref()
            viewModel.userSession.observe(this@SplashCompose){
                if (it.id != DataStoreManager.DEFAULT_ID){
                    startActivity(Intent(this@SplashCompose, MainActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashCompose, LoginCompose::class.java))
                    finish()
                }
                }

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(all = 100.dp)
                .fillMaxSize()
        ) {
            loader()
        }
    }

    @Composable
    private fun loader() {
        val compositionResult: LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.pokemon)
        )
        val progress by animateLottieCompositionAsState(
            compositionResult.value,
            isPlaying = true,
            iterations = 1,
            speed = 1.0f
        )
        LottieAnimation(
            compositionResult.value,
            progress
        )
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun DefaultPreview() {
        PokemonTheme {
            Column {
                SplashScreen()
            }
        }
    }
}

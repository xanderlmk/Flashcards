package com.example.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcards.controller.AppViewModelProvider
import com.example.flashcards.controller.MainViewModel
import com.example.flashcards.ui.theme.FlashcardsTheme
import com.example.flashcards.views.MainView

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        AppViewModelProvider.Factory
    }
    private var view = MainView()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    view.DeckList(viewModel = viewModel, Modifier.padding(innerPadding))

                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashcardsTheme {

        var view = MainView()
        view.DeckList(viewModel = MainViewModel(/* Pass a mock or test repository here */))
    }
}*/
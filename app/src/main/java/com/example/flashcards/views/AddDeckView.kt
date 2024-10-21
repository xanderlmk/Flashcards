package com.example.flashcards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.flashcards.controller.MainViewModel


class AddDeckView(private var viewModel: MainViewModel) {


    @Composable
    fun AddDeck(onDismiss: () -> Unit) {
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var deckName by remember {mutableStateOf("")  }

            Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add deck",
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                lineHeight = 116.sp
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EditTextField(
                    value = deckName,
                    onValueChanged = { newText ->
                        deckName = newText
                    },
                    labelStr = "Deck Name",
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                       /* if (viewModel.addDeck(deckName)) {
                            deckName = ""
                            onDismiss()
                        }
                        else {
                            errorMessage = "Deck name must be unique"
                        }*/
                        viewModel.addDeck(deckName)
                        deckName = ""
                        onDismiss()
                    },
                    modifier = Modifier.padding(top = 48.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }


}
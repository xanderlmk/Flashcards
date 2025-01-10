package com.example.flashcards.views.cardViews.addCardViews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.R
import com.example.flashcards.controller.viewModels.cardViewsModels.AddCardViewModel
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.model.uiModels.Fields
import com.example.flashcards.views.miscFunctions.EditTextField
import com.example.flashcards.ui.theme.GetModifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddThreeCard(vm : AddCardViewModel,deck: Deck,
                 fields: Fields, getModifier: GetModifier) {
    val errorMessage by vm.errorMessage.collectAsState()
    var successMessage by remember { mutableStateOf("") }
    val fillOutFields = stringResource(R.string.fill_out_all_fields).toString()
    val cardAdded = stringResource(R.string.card_added).toString()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(R.string.question),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            color = getModifier.titleColor(),
            modifier = Modifier
                .padding(top = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            EditTextField(
                value = fields.question.value,
                onValueChanged = { newText ->
                    fields.question.value =
                        newText
                },
                labelStr = stringResource(R.string.question),
                modifier = Modifier
                    .weight(1f)
            )
        }
        Text(
            text = stringResource(R.string.middle_field),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            color = getModifier.titleColor(),
            modifier = Modifier
                .padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            EditTextField(
                value = fields.middleField.value,
                onValueChanged = { newText ->
                    fields.middleField.value =
                        newText
                },
                labelStr = stringResource(R.string.middle_field),
                modifier = Modifier
                    .weight(1f)
            )
        }
        Text(
            text = stringResource(R.string.answer),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            color = getModifier.titleColor(),
            modifier = Modifier.padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            EditTextField(
                value = fields.answer.value,
                onValueChanged = { newText ->
                    fields.answer.value =
                        newText
                },
                labelStr = stringResource(R.string.answer),
                modifier = Modifier
                    .weight(1f)
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(4.dp),
                fontSize = 16.sp
            )
        } else {
            Spacer(modifier = Modifier.padding(16.dp))
        }
        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = getModifier.titleColor(),
                modifier = Modifier.padding(4.dp),
                fontSize = 16.sp
            )
        } else {
            Spacer(modifier = Modifier.padding(16.dp))
        }
        LaunchedEffect(successMessage) {
            delay(1500)
            successMessage = ""
            coroutineScope.launch {
                scrollState.animateScrollTo(0)
            }
        }
        LaunchedEffect(errorMessage) {
            delay(1500)
            vm.clearErrorMessage()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (fields.question.value.isBlank() ||
                        fields.answer.value.isBlank() ||
                        fields.middleField.value.isBlank()
                    ) {
                        vm.setErrorMessage(fillOutFields)
                        successMessage = ""
                    } else {
                        vm.addThreeCard(
                            deck, fields.question.value,
                            fields.middleField.value, fields.answer.value
                        )
                        fields.question.value = ""
                        fields.middleField.value = ""
                        fields.answer.value = ""
                        successMessage = cardAdded
                    }
                },
                modifier = Modifier.padding(top = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = getModifier.secondaryButtonColor(),
                    contentColor = getModifier.buttonTextColor()
                )
            ) {
                Text(stringResource(R.string.submit))
            }
        }
    }
}
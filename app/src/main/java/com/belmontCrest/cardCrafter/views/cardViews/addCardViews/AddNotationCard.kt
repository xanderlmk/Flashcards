package com.belmontCrest.cardCrafter.views.cardViews.addCardViews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.belmontCrest.cardCrafter.R
import com.belmontCrest.cardCrafter.controller.viewModels.cardViewsModels.AddCardViewModel
import com.belmontCrest.cardCrafter.localDatabase.tables.Deck
import com.belmontCrest.cardCrafter.model.uiModels.Fields
import com.belmontCrest.cardCrafter.ui.theme.GetUIStyle
import com.belmontCrest.cardCrafter.uiFunctions.LatexKeyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddNotationCard(
    vm: AddCardViewModel, deck: Deck,
    fields: Fields, getUIStyle: GetUIStyle
) {
    val errorMessage by vm.errorMessage.collectAsStateWithLifecycle()
    var successMessage by remember { mutableStateOf("") }
    val fillOutFields = stringResource(R.string.fill_out_all_fields).toString()
    val cardAdded = stringResource(R.string.card_added).toString()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var steps by rememberSaveable { mutableIntStateOf(0) }
    fields.stringList  = rememberSaveable { mutableListOf() }
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
            color = getUIStyle.titleColor(),
            modifier = Modifier
                .padding(top = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            LatexKeyboard(
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
            text = "Steps",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            color = getUIStyle.titleColor(),
            modifier = Modifier.padding(top = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (steps == 0) {
                Text(
                    text = "Add a step",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            fields.stringList.add(mutableStateOf(""))
                            steps = 1
                        }
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            } else {
                var index = 0
                fields.stringList.forEach {
                    index += 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LatexKeyboard(
                            value = it.value,
                            onValueChanged = { newText ->
                                it.value = newText
                            },
                            labelStr = "Step: $index",
                            modifier = Modifier
                                .weight(1f)
                                .padding(0.5.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Add a step",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                fields.stringList.add(mutableStateOf(""))
                                steps += 1
                            }
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
                Button(
                    onClick = {
                        if (steps > 0) {
                            steps -= 1
                            fields.stringList.removeAt(steps)
                        }
                    },
                    modifier = Modifier.padding(top = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getUIStyle.secondaryButtonColor(),
                        contentColor = getUIStyle.buttonTextColor()
                    )
                ) {
                    Text("Remove Step")
                }
            }
        }
        Text(
            text = stringResource(R.string.answer),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            color = getUIStyle.titleColor(),
            modifier = Modifier.padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            LatexKeyboard(
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

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (
                        fields.question.value.isBlank() ||
                        fields.answer.value.isBlank()
                    ) {
                        vm.setErrorMessage(fillOutFields)
                        successMessage = ""
                    } else if (
                        fields.stringList.isNotEmpty() &&
                        fields.stringList.all { it.value.isBlank() }
                    ) {
                        vm.setErrorMessage("Steps can't be blank")
                        successMessage = ""
                    } else {
                        coroutineScope.launch {
                            vm.addNotationCard(
                                deck, fields.question.value,
                                fields.stringList.map { it.value }, fields.answer.value
                            )
                            fields.question.value = ""
                            fields.stringList.clear()
                            steps = 0
                            fields.answer.value = ""
                            successMessage = cardAdded
                        }
                    }
                },
                modifier = Modifier.padding(top = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = getUIStyle.secondaryButtonColor(),
                    contentColor = getUIStyle.buttonTextColor()
                )
            ) {
                Text(stringResource(R.string.submit))
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(4.dp),
                fontSize = 16.sp
            )
        }
        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = getUIStyle.titleColor(),
                modifier = Modifier.padding(4.dp),
                fontSize = 16.sp
            )
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
    }
}
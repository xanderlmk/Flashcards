package com.example.flashcards.views.miscFunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcards.R

import com.example.flashcards.model.tablesAndApplication.BasicCard
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.model.tablesAndApplication.HintCard
import com.example.flashcards.model.tablesAndApplication.ThreeFieldCard
import com.example.flashcards.ui.theme.textColor
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.example.flashcards.model.tablesAndApplication.MultiChoiceCard
import com.example.flashcards.model.uiModels.CardListUiState
import com.example.flashcards.model.uiModels.CardUiState
import com.example.flashcards.model.uiModels.Fields
import com.example.flashcards.ui.theme.GetModifier


@Composable
fun EditTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    labelStr: String,
    modifier: Modifier,
    inputColor: Color = Color.Transparent
) {
    val focusManager = LocalFocusManager.current
    val colors = if (inputColor == Color.Transparent) {
        TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer

        )
    } else {
        TextFieldDefaults.colors(
            unfocusedTextColor = inputColor,
            focusedTextColor = inputColor,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    }
    TextField(
        value = value,
        singleLine = false,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(labelStr, color = textColor) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = colors,
        textStyle =
        if (inputColor == Color.Transparent) {
            TextStyle.Default
        } else {
            TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                background = MaterialTheme.colorScheme.surface
            )
        }
    )
}

@Composable
fun EditDoubleField(
    value: String,
    onValueChanged: (String) -> Unit,
    labelStr: String,
    modifier: Modifier
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer

    )
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(labelStr, color = textColor) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = colors

    )
}

@Composable
fun EditIntField(
    value: String,
    onValueChanged: (String) -> Unit,
    labelStr: String,
    modifier: Modifier
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
    )
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = {
            Text(
                labelStr, color = textColor, fontSize = 12.sp
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = colors,
    )
}


@Composable
fun NoDueCards(getModifier: GetModifier) {
    var delay by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(150)
        delay = true
    }
    if (delay) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.no_due_cards),
                fontSize = 25.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center,
                color = getModifier.titleColor(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun BasicCardQuestion(basicCard: BasicCard) {
    Text(
        text = stringResource(R.string.question) + ": ${basicCard.question}",
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ThreeCardQuestion(threeFieldCard: ThreeFieldCard) {
    Text(
        text = stringResource(R.string.question) + ": ${threeFieldCard.question}",
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun HintCardQuestion(hintCard: HintCard) {
    Text(
        text = stringResource(R.string.question) + ": ${hintCard.question}",
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ChoiceCardQuestion(multiChoiceCard: MultiChoiceCard) {
    Text(
        text = stringResource(R.string.question) + ": ${multiChoiceCard.question}",
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ShowBackButtonAndDeckName(
    onNavigate: () -> Unit,
    deck: Deck,
    presetModifier: Modifier,
    getModifier: GetModifier
) {
    Row {
        BackButton(
            onBackClick = {
                onNavigate()
            },
            modifier = presetModifier
                .fillMaxSize(),
            getModifier = getModifier
        )
        Text(
            text = stringResource(R.string.deck) + ": ${deck.name}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 35.sp,
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = getModifier.buttonTextColor()
        )
    }
}

@Composable
fun CardSelector(
    cardListUiState: CardListUiState,
    cardUiState: CardUiState,
    index: Int
) {
    if (cardListUiState.allCards.size == cardUiState.cardList.size) {
        when (cardUiState.cardList[index].type) {
            "basic" -> {
                val basicCard =
                    cardListUiState.allCards[index].basicCard
                basicCard?.let { BasicCardQuestion(basicCard) }
            }

            "three" -> {
                val threeCard =
                    cardListUiState.allCards[index].threeFieldCard
                threeCard?.let { ThreeCardQuestion(threeCard) }
            }

            "hint" -> {
                val hintCard =
                    cardListUiState.allCards[index].hintCard
                hintCard?.let { HintCardQuestion(hintCard) }
            }

            "multi" -> {
                val choiceCard =
                    cardListUiState.allCards[index].multiChoiceCard
                choiceCard?.let { ChoiceCardQuestion(choiceCard) }
            }
        }
    }
}

@Composable
fun PickAnswerChar(fields: Fields, getModifier: GetModifier) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = stringResource(R.string.answer) +
                            ": ${fields.correct.value.uppercase()}",
                    modifier = Modifier.padding(2.dp)
                )
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Answer",
                    tint = getModifier.titleColor(),
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
        Box(
            Modifier.fillMaxWidth(.25f),
            contentAlignment = Alignment.BottomEnd
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        fields.correct.value = 'a'
                        expanded = false
                    },
                    text = { Text("A") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                DropdownMenuItem(
                    onClick = {
                        fields.correct.value = 'b'
                        expanded = false
                    },
                    text = { Text("B") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                if (fields.choices[2].value.isNotBlank()) {
                    DropdownMenuItem(
                        onClick = {
                            fields.correct.value = 'c'
                            expanded = false
                        },
                        text = { Text("C") },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
                if (fields.choices[3].value.isNotBlank()) {
                    DropdownMenuItem(
                        onClick = {
                            fields.correct.value = 'd'
                            expanded = false
                        },
                        text = { Text("D") },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun returnReviewError(): List<String> {
    return listOf(
        stringResource(R.string.review_amount_0).toString(),
        stringResource(R.string.review_amount_10).toString(),
        stringResource(R.string.review_amount_same).toString(),
        stringResource(R.string.failed_review).toString()
    )
}

@Composable
fun returnMultiplierError(): List<String> {
    return listOf(
        stringResource(R.string.good_multiplier_1).toString(),
        stringResource(R.string.bad_multiplier_1).toString(),
        stringResource(R.string.multipliers_same).toString(),
        stringResource(R.string.failed_multiplier).toString()
    )
}

@Composable
fun returnDeckError(): List<String> {
    return listOf(
        stringResource(R.string.empty_deck_name).toString(),
        stringResource(R.string.deck_name_exists).toString(),
        stringResource(R.string.deck_name_failed).toString()
    )
}

suspend fun delayNavigate() {
    delay(85)
}

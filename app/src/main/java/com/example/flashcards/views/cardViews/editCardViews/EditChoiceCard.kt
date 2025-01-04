package com.example.flashcards.views.cardViews.editCardViews


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.flashcards.R
import com.example.flashcards.model.tablesAndApplication.MultiChoiceCard
import com.example.flashcards.model.uiModels.Fields
import com.example.flashcards.ui.theme.GetModifier
import com.example.flashcards.views.miscFunctions.EditTextField
import com.example.flashcards.views.miscFunctions.PickAnswerChar

@Composable
fun EditChoiceCard(
    multiChoiceCard: MultiChoiceCard,
    fields: Fields,
    getModifier: GetModifier
) {
    fields.question = remember { mutableStateOf(multiChoiceCard.question) }
    fields.choices[0] = remember { mutableStateOf(multiChoiceCard.choiceA) }
    fields.choices[1] = remember { mutableStateOf(multiChoiceCard.choiceB) }
    fields.choices[2] = remember { mutableStateOf(multiChoiceCard.choiceC) }
    fields.choices[3] = remember { mutableStateOf(multiChoiceCard.choiceD) }
    fields.correct = remember { mutableStateOf(multiChoiceCard.correct) }


    EditTextField(
        value = fields.question.value,
        onValueChanged = { fields.question.value = it },
        labelStr = stringResource(R.string.question),
        modifier = Modifier.fillMaxWidth()
    )
    EditTextField(
        value = fields.choices[0].value,
        onValueChanged = { fields.choices[0].value = it },
        labelStr = stringResource(R.string.choice_a),
        modifier = Modifier.fillMaxWidth()
    )
    EditTextField(
        value = fields.choices[1].value,
        onValueChanged = { fields.choices[1].value = it },
        labelStr = stringResource(R.string.choice_b),
        modifier = Modifier.fillMaxWidth()
    )
    EditTextField(
        value = fields.choices[2].value,
        onValueChanged = { fields.choices[2].value = it },
        labelStr = stringResource(R.string.choice_c),
        modifier = Modifier.fillMaxWidth()
    )
    EditTextField(
        value = fields.choices[3].value,
        onValueChanged = { fields.choices[3].value = it },
        labelStr = stringResource(R.string.choice_d),
        modifier = Modifier.fillMaxWidth()
    )
    PickAnswerChar(fields, getModifier)
}
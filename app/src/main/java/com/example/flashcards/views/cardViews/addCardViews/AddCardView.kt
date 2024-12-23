package com.example.flashcards.views.cardViews.addCardViews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.controller.viewModels.BasicCardViewModel
import com.example.flashcards.controller.viewModels.HintCardViewModel
import com.example.flashcards.controller.viewModels.ThreeCardViewModel
import com.example.flashcards.model.Fields
import com.example.flashcards.views.miscFunctions.BackButton
import com.example.flashcards.views.miscFunctions.GetModifier

class AddCardView(
    private var fields: Fields,
    private var cardTypes : Triple<BasicCardViewModel, ThreeCardViewModel,
            HintCardViewModel>,
    private var getModifier: GetModifier) {
    @Composable
    fun AddCard(deckId: Int, onNavigate: () -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val type = remember { mutableStateOf("basic") }
        val presetModifier = Modifier
            .padding(top = 16.dp,start = 16.dp, end = 16.dp)
            .size(54.dp)

        Box(
            modifier = getModifier.boxViewsModifier()
        ) {
            BackButton(
                onBackClick = {
                    fields.question.value = ""
                    fields.middleField.value = ""
                    fields.answer.value = ""
                    onNavigate()
                },
                modifier = presetModifier,
                getModifier = getModifier
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box( Modifier.fillMaxWidth().wrapContentSize(Alignment.TopEnd)){
                IconButton(onClick = { expanded = true },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(54.dp)) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Card Type",
                        tint = getModifier.titleColor()
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(onClick = { type.value = "basic"}, text = {Text("Basic")})
                    DropdownMenuItem(onClick = { type.value = "three" }, text = {Text("Three Fields")})
                    DropdownMenuItem(onClick = { type.value = "hint" }, text = {Text("Hint")})
                }
                }
                val text = type.value.replaceFirstChar { it.uppercaseChar() }
                Text(
                    text = text,
                    fontSize = 35.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    color = getModifier.titleColor(),
                    fontWeight = FontWeight.Bold,
                )
                when (type.value) {
                    "three" -> AddThreeCard(cardTypes.second, deckId,
                        fields,getModifier)
                    "hint"  -> AddHintCard(cardTypes.third, deckId,
                        fields, getModifier)
                    else -> AddBasicCard(cardTypes.first, deckId,
                        fields, getModifier)
                }
            }
        }
    }
}
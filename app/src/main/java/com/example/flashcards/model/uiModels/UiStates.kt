package com.example.flashcards.model.uiModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flashcards.model.tablesAndApplication.AllCardTypes
import com.example.flashcards.model.tablesAndApplication.BasicCardType
import com.example.flashcards.model.tablesAndApplication.Card
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.model.tablesAndApplication.HintCardType
import com.example.flashcards.model.tablesAndApplication.MultiChoiceCardType
import com.example.flashcards.model.tablesAndApplication.ThreeCardType

data class DeckUiState(
    val deckList: List<Deck> = listOf()
)
data class CardListUiState(
    var allCards: List<AllCardTypes> = emptyList(),
    var errorMessage: String = ""
)
data class CardUiState(
    var cardList: List<Card> = emptyList(),
    val loading: MutableState<Boolean> = mutableStateOf(false),
    var errorMessage: String = ""
)
data class BasicCardUiState(
    var basicCards: List<BasicCardType> = emptyList(),
    var errorMessage: String = "",
)
data class HintCardUiState(
    var hintCards: List<HintCardType> = emptyList(),
    var errorMessage: String = "",
)
data class ThreeCardUiState(
    var threeFieldCards: List<ThreeCardType> = emptyList(),
    var errorMessage: String = "",
)
data class MultiChoiceUiCardState(
    var multiChoiceCard: List<MultiChoiceCardType> = emptyList(),
    var errorMessage: String = "",
)

sealed class CardState {
    object Idle : CardState()
    object Loading : CardState()
    //data class ShowingCard(val card: Card) : CardState()
    object Finished : CardState()
    //data class Error(val message: String) : CardState()
}



package com.example.flashcards.views.cardViews.editCardViews

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcards.model.tablesAndApplication.Card
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.controller.viewModels.cardViewsModels.EditingCardListViewModel
import com.example.flashcards.model.uiModels.Fields
import com.example.flashcards.views.miscFunctions.CardSelector
import com.example.flashcards.ui.theme.GetModifier
import com.example.flashcards.views.miscFunctions.ShowBackButtonAndDeckName

class EditCardsList(
    private var editingCardListVM: EditingCardListViewModel,
    private var fields: Fields,
    private var listState: LazyListState,
    private val selectedCard: MutableState<Card?>,
    private var getModifier: GetModifier
) {
    var isEditing = mutableStateOf(false)

    @SuppressLint("CoroutineCreationDuringComposition")

    @Composable
    fun ViewFlashCards(
        deck: Deck, onNavigate: () -> Unit, goToEditCard: () -> Unit
    ) {
        //var deckWithCards by remember { mutableStateOf(DeckWithCards(Deck(0, loading.toString() ), emptyList())) }
        val cardListUiState by editingCardListVM.cardListUiState.collectAsState()
        val middleCard = rememberSaveable { mutableIntStateOf(0) }
        var clicked by remember { mutableStateOf(false) }

        val presetModifier = getModifier.backButtonModifier()

        // Restore the scroll position when returning from editing
        LaunchedEffect(Unit) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    middleCard.intValue = visibleItems.size / 2
                }
            getListState(listState, middleCard.intValue)
        }
        Box(
            modifier = getModifier.boxViewsModifier(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Box(
                    modifier = getModifier.bottomLineModifier()
                ) {
                    ShowBackButtonAndDeckName(
                        onNavigate, deck, presetModifier, getModifier
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState
                ) {
                    items(cardListUiState.allCards.size) { index ->
                        Button(
                            onClick = {
                                if (!clicked) {
                                    fields.scrollPosition.value = index
                                    selectedCard.value =
                                        cardListUiState.allCards[index].card
                                    isEditing.value = true
                                    clicked = true
                                    goToEditCard()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = getModifier.secondaryButtonColor(),
                                contentColor = getModifier.buttonTextColor()
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            CardSelector(
                                cardListUiState,
                                index
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun getListState(listState: LazyListState, middleCard: Int) {
        if (fields.scrollPosition.value == 0) {
            Log.d("scrollPosition", "${fields.scrollPosition.value}")
            listState.scrollToItem(0)
        } else if (listState.firstVisibleItemScrollOffset == 0 &&
            fields.scrollPosition.value > 0
        ) {
            listState.scrollToItem(listState.firstVisibleItemIndex)
        } else if (fields.scrollPosition.value >
            listState.layoutInfo.visibleItemsInfo.lastIndex - middleCard ||
            fields.scrollPosition.value <=
            listState.layoutInfo.visibleItemsInfo.lastIndex - middleCard
        ) {
            listState.scrollToItem(
                listState.layoutInfo.visibleItemsInfo.lastIndex - middleCard
            )
        } else {
            listState.scrollToItem(fields.scrollPosition.value)
        }
    }
}
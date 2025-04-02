package com.belmontCrest.cardCrafter.views.cardViews.editCardViews

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.belmontCrest.cardCrafter.controller.cardHandlers.returnCardId
import com.belmontCrest.cardCrafter.controller.viewModels.cardViewsModels.EditingCardListViewModel
import com.belmontCrest.cardCrafter.model.uiModels.Fields
import com.belmontCrest.cardCrafter.views.miscFunctions.CardSelector
import com.belmontCrest.cardCrafter.ui.theme.GetUIStyle
import com.belmontCrest.cardCrafter.ui.theme.boxViewsModifier

class EditCardsList(
    private var editingCardListVM: EditingCardListViewModel,
    private var fields: Fields,
    private var listState: LazyListState,
    private var getUIStyle: GetUIStyle
) {
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun ViewFlashCards(
        goToEditCard: (Int, Int) -> Unit
    ) {
        val sealedCardsList by editingCardListVM.sealedAllCTs.collectAsStateWithLifecycle()
        val middleCard = rememberSaveable { mutableIntStateOf(0) }
        var clicked by remember { mutableStateOf(false) }
        // Restore the scroll position when returning from editing
        LaunchedEffect(Unit) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    middleCard.intValue = visibleItems.size / 2
                }
            getListState(listState, middleCard.intValue)
        }
        Box(
            modifier = Modifier
                .boxViewsModifier(getUIStyle.getColorScheme()),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                items(sealedCardsList.allCTs.size) { index ->
                    Button(
                        onClick = {
                            if (!clicked) {
                                fields.scrollPosition.value = index
                                fields.isEditing.value = true
                                clicked = true
                                goToEditCard(index, returnCardId(sealedCardsList.allCTs[index]))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getUIStyle.secondaryButtonColor(),
                            contentColor = getUIStyle.buttonTextColor()
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        CardSelector(
                            sealedCardsList,
                            index
                        )
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
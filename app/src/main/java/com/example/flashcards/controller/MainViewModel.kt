package com.example.flashcards.controller
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.model.Decks
import com.example.flashcards.model.FlashCardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class MainViewModel(private val decksRepository: FlashCardRepository) : ViewModel() {


    val mainUiState: StateFlow<MainUiState> =
        decksRepository.getAllDecksStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    /*fun addDeck(name: String): Boolean {
        var success : Boolean = false

        if (name.isNotEmpty()) {
            try {
                viewModelScope.launch {
                    decksRepository.insertDeck(Decks(name = name))
                }
                success = true
            } catch(e: SQLiteConstraintException) {
                success = false
            }
        }
        return success
    }*/
    fun addDeck(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                decksRepository.insertDeck(Decks(name = name))
            }
        }
    }

        // Deleting a deck via the repository
        fun deleteDeck(deck: Decks) {
            viewModelScope.launch {
                decksRepository.deleteDeck(deck)
            }
        }

    }



data class MainUiState(val deckList: List<Decks> = listOf())



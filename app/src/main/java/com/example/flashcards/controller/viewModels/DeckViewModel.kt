package com.example.flashcards.controller.viewModels

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.model.repositories.CardTypeRepository
import com.example.flashcards.model.repositories.FlashCardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.flashcards.model.tablesAndApplication.Deck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

/**
 * ViewModel to retrieve all items in the Room database.
 */
class DeckViewModel(private val flashCardRepository: FlashCardRepository,
    private val cardTypeRepository: CardTypeRepository) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)

    val mainUiState: StateFlow<MainUiState> =
        flashCardRepository.getAllDecksStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun checkIfDeckExists(name: String): Int {
        return withContext(Dispatchers.IO) {
            try {
                flashCardRepository.checkIfDeckExists(name)
            } catch (e: SQLiteConstraintException) {
                _errorMessage.value = "Error checking deck existence: ${e.message}"
                0
            }
        }
    }

    fun addDeck(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    flashCardRepository.insertDeck(Deck(name = name))
                } catch (e: SQLiteConstraintException) {
                    _errorMessage.value = "A deck with this name already exists"
                    println(e)
                } catch (e: Exception){
                    _errorMessage.value = "error adding deck: ${e.message}"
                }
            }
        }
    }

    // Deleting a deck via the repository
    fun deleteDeck(deck: Deck) {
        viewModelScope.launch{
            flashCardRepository.deleteAllCards(deck.id)
            flashCardRepository.deleteDeck(deck)
        }
    }

    suspend fun updateDeckName(newName: String, deckID: Int): Int {
        return withContext(Dispatchers.IO) {
            try {
                val rowsUpdated = flashCardRepository.updateDeckName(newName, deckID)
                if (rowsUpdated == 0) {
                    _errorMessage.value = "Failed to update deck name - name may already exist"
                }
                rowsUpdated
            } catch (e: SQLiteConstraintException) {
                _errorMessage.value = "A deck with this name already exists"
                println(e)
                0
            } catch (e: Exception) {
                _errorMessage.value = "Error updating deck name: ${e.message}"
                0
            }
        }
    }
    suspend fun getDeckById(deckId : Int) : Flow<Deck?> {
        return flashCardRepository.getDeckStream(deckId)
    }
}

data class MainUiState(val deckList: List<Deck> = listOf())



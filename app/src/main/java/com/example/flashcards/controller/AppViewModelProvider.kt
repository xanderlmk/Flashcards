package com.example.flashcards.controller

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcards.controller.navigation.NavViewModel
import com.example.flashcards.supabase.controller.SupabaseViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.EditingCardListViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.MainViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.CardDeckViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.AddCardViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.EditCardViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.AddDeckViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.DeckViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.EditDeckViewModel
import com.example.flashcards.model.tablesAndApplication.FlashCardApplication
import com.example.flashcards.supabase.controller.APIViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire  app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NavViewModel(
                flashCardApplication().container.flashCardRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            MainViewModel(
                flashCardApplication().container.flashCardRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            AddDeckViewModel(
                flashCardApplication().container.flashCardRepository,
            )
        }
        initializer {
            DeckViewModel(
                flashCardApplication().container.flashCardRepository
            )
        }
        initializer {
            EditDeckViewModel(
                flashCardApplication().container.flashCardRepository,
            )
        }
        initializer {
            AddCardViewModel(
                flashCardApplication().container.flashCardRepository,
                flashCardApplication().container.cardTypeRepository,
                flashCardApplication().container.scienceSpecificRepository
            )
        }
        initializer {
            CardDeckViewModel(
                flashCardApplication().container.flashCardRepository,
                flashCardApplication().container.cardTypeRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            EditingCardListViewModel(
                flashCardApplication().container.cardTypeRepository
            )
        }
        initializer {
            EditCardViewModel(
                flashCardApplication().container.flashCardRepository,
                flashCardApplication().container.cardTypeRepository,
                flashCardApplication().container.scienceSpecificRepository
            )
        }
        initializer {
            SupabaseViewModel(
                flashCardApplication().container.flashCardRepository,
                flashCardApplication().container.cardTypeRepository,
                flashCardApplication().container.scienceSpecificRepository
            )
        }
        initializer {
            APIViewModel()
        }
    }
}


fun CreationExtras.flashCardApplication(): FlashCardApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FlashCardApplication)
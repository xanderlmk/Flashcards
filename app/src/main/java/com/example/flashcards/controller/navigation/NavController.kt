package com.example.flashcards.controller.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcards.views.cardViews.addCardViews.AddCardView
import com.example.flashcards.views.deckViews.AddDeckView
import com.example.flashcards.views.cardViews.cardDeckViews.CardDeckView
import com.example.flashcards.views.cardViews.editCardViews.EditCardsList
import com.example.flashcards.views.deckViews.DeckView
import com.example.flashcards.views.MainView
import com.example.flashcards.model.uiModels.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navigation
import com.example.flashcards.controller.AppViewModelProvider
import com.example.flashcards.controller.updateDecksCardList
import com.example.flashcards.controller.viewModels.NavViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.EditingCardListViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.MainViewModel
import com.example.flashcards.controller.viewModels.cardViewsModels.CardDeckViewModel
import com.example.flashcards.controller.viewModels.deckViewsModels.AddDeckViewModel
import com.example.flashcards.model.uiModels.Fields
import com.example.flashcards.model.tablesAndApplication.Card
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.model.uiModels.BasicCardUiState
import com.example.flashcards.model.uiModels.HintCardUiState
import com.example.flashcards.model.uiModels.MultiChoiceUiCardState
import com.example.flashcards.model.uiModels.PreferencesManager
import com.example.flashcards.model.uiModels.ThreeCardUiState
import com.example.flashcards.ui.theme.ColorSchemeClass
import com.example.flashcards.views.GeneralSettings
import com.example.flashcards.views.deckViews.EditDeckView
import com.example.flashcards.views.cardViews.editCardViews.EditingCardView
import com.example.flashcards.ui.theme.GetModifier
import kotlinx.coroutines.launch

data class AllTypesUiStates(
    val basicCardUiState: BasicCardUiState,
    val hintUiStates: HintCardUiState,
    val threeCardUiState: ThreeCardUiState,
    val multiChoiceUiCardState: MultiChoiceUiCardState,
)


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    editingCardListVM: EditingCardListViewModel,
    fields: Fields,
    modifier: Modifier = Modifier,
    preferences: PreferencesManager
) {
    val cardDeckVM: CardDeckViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val navViewModel: NavViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val basicCardUiState by
    editingCardListVM.basicCardUiState.collectAsState()
    val hintCardUiState by
    editingCardListVM.hintCardUiState.collectAsState()
    val threeCardUiState by
    editingCardListVM.threeCardUiState.collectAsState()
    val multiCardUiState by
    editingCardListVM.multiChoiceUiState.collectAsState()
    val allTypesUiStates =
        AllTypesUiStates(
            basicCardUiState,
            hintCardUiState,
            threeCardUiState,
            multiCardUiState
        )

    val cardsToUpdate by cardDeckVM.cardListToUpdate.collectAsState()
    val cardList by cardDeckVM.sealedDueCTs.collectAsState()
    val listState = rememberLazyListState()
    val colorScheme = remember { ColorSchemeClass() }
    var onDeckView by remember { mutableStateOf(false) }
    colorScheme.colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = isSystemInDarkTheme()

    val getModifier = remember { GetModifier(colorScheme, isDarkTheme) }
    val view = remember { View() }
    val selectedCard: MutableState<Card?> = rememberSaveable { mutableStateOf(null) }


    val cardDeckView = CardDeckView(
        cardDeckVM, getModifier
    )
    val editDeckView = EditDeckView(fields, getModifier)
    val deckEditView =
        EditCardsList(
            editingCardListVM,
            fields, listState,
            selectedCard, getModifier
        )
    val editingCardView = EditingCardView(
        editingCardListVM, allTypesUiStates, getModifier
    )
    val mainView = MainView(getModifier, fields)
    val addDeckView = AddDeckView(getModifier)
    val deckView = DeckView(
        fields, getModifier
    )
    val addCardView = AddCardView(fields, getModifier)
    val generalSettings = GeneralSettings(getModifier, preferences)

    val coroutineScope = rememberCoroutineScope()
    val deck = navViewModel.deck.collectAsState()

    NavHost(
        navController = navController,
        startDestination = DeckListDestination.route,
        modifier = modifier
    ) {
        composable(DeckListDestination.route) {
            BackHandler {
                // Exit the app when back is pressed on the main screen
                (navController.context as? Activity)?.finish()
            }
            mainView.DeckList(
                mainViewModel,
                // In DeckList Composable
                onNavigateToDeck = { id ->
                    Log.d("Nav Controller", "Navigating into to deck.$id")
                    fields.scrollPosition.value = 0
                    onDeckView = true
                    navController.navigate(DeckOptionsDestination.createRoute(id))
                    coroutineScope.launch {
                        navViewModel.getDeckById(id)
                        cardDeckVM.getDueCards(id)
                    }
                    coroutineScope.launch {
                        editingCardListVM.getAllCardsForDeck(id)
                    }
                },
                onNavigateToAddDeck = {
                    navController.navigate(AddDeckDestination.route)
                },
                onNavigateToSettings = {
                    navController.navigate(SettingsDestination.route)
                }
            )
        }
        composable(SettingsDestination.route) {
            BackHandler {
                fields.mainClicked.value = false
                navController.popBackStack(
                    DeckListDestination.route, inclusive = false
                )
            }
            generalSettings.SettingsView(
                onNavigate = {
                    fields.mainClicked.value = false
                    navController.navigate(DeckListDestination.route)
                }
            )
        }
        composable(AddDeckDestination.route) {
            BackHandler {
                view.whichView.intValue = 0
                view.onView.value = false
                fields.mainClicked.value = false
                navController.popBackStack(
                    DeckListDestination.route,
                    inclusive = false
                )
            }
            addDeckView.AddDeck(
                onNavigate = {
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.mainClicked.value = false
                    navController.navigate(DeckListDestination.route)
                }
            )
        }
        navigation(
            route = DeckOptionsDestination.route,
            startDestination = DeckViewDestination.route
        ) {
            composable(
                route = DeckViewDestination.route
            ) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")!!.toIntOrNull()
                BackHandler {
                    fields.scrollPosition.value = 0
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.mainClicked.value = false
                    navController.popBackStack(
                        DeckListDestination.route,
                        inclusive = false
                    )
                }
                /**
                 * If the application restarts, onDeckView value will be false
                 * meaning it'll get the latest value of deck if it changes,
                 * it'll stop getting the value once you return to the DeckList
                 * and go back into the DeckOptions, since onDeckView will become
                 * true. */
                LaunchedEffect(Unit) {
                    if (!onDeckView) {
                        coroutineScope.launch {
                            navViewModel.getDeckById(deckId ?: 0)
                        }
                    }
                }
                Log.d("Nav Controller", "ViewEditDeck - Deck Value: ${deck.value}")

                deck.value?.let {
                    deckView.ViewEditDeck(
                        deck = it,
                        onNavigate = {
                            fields.scrollPosition.value = 0
                            view.whichView.intValue = 0
                            view.onView.value = false
                            fields.mainClicked.value = false
                            navController.navigate(DeckListDestination.route)
                        },
                        whichView = view,
                        goToAddCard = { id ->
                            fields.mainClicked.value = false
                            navController.navigate(AddCardDestination.createRoute(id))
                            view.onView.value = true
                        },
                        goToViewCard = { id ->
                            fields.mainClicked.value = false
                            navController.navigate(ViewCardDestination.createRoute(id))
                            view.onView.value = true
                        },
                        goToEditDeck = { id, name ->
                            fields.mainClicked.value = false
                            navController.navigate(
                                EditDeckDestination.createRoute(
                                    id,
                                    name
                                )
                            )
                            view.onView.value = true
                        },
                        goToViewCards = { id ->
                            fields.mainClicked.value = false
                            navController.navigate(ViewAllCardsDestination.createRoute(id))
                            view.onView.value = true
                        },
                    )
                }
            }
            composable(AddCardDestination.route) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")!!.toIntOrNull()

                BackHandler {
                    /**
                     * User added a card to get to Studying! lol
                     */
                    if(cardList.allCTs.isEmpty()) {
                        coroutineScope.launch {
                            cardDeckVM.getDueCards(deckId ?: 0)
                        }
                    }
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.inDeckClicked.value = false
                    fields.resetFields()
                    navController.popBackStack(
                        DeckViewDestination.createRoute(deckId ?: 0),
                        inclusive = false
                    )
                }
                // Pass the deckId to AddCard composable
                deck.value?.let {
                    addCardView.AddCard(
                        deck = it,
                        onNavigate = {
                            /**
                             * User added a card to get to Studying! lol
                             */
                            if (cardList.allCTs.isEmpty()) {
                                coroutineScope.launch {
                                    cardDeckVM.getDueCards(deckId ?: 0)
                                }
                            }
                            view.whichView.intValue = 0
                            view.onView.value = false
                            fields.inDeckClicked.value = false
                            fields.resetFields()
                            navController.navigate(DeckViewDestination.createRoute(deckId ?: 0))
                        }
                    )
                }
            }
            composable(ViewCardDestination.route) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")!!.toIntOrNull()

                BackHandler {
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.inDeckClicked.value = false
                    getModifier.clickedChoice.value = '?'
                    navController.popBackStack(
                        DeckViewDestination.createRoute(deckId ?: 0),
                        inclusive = false
                    )
                    deck.value?.let {
                        coroutineScope.launch {
                            /** This function also gets the due cards */
                            updateDecksCardList(
                                it,
                                cardsToUpdate,
                                cardDeckVM
                            )
                        }
                    }
                }
                Log.d("Nav Controller", "ViewCard - Deck Value: ${deck.value}")
                deck.value?.let {
                    cardDeckView.ViewCard(
                        deck = it,
                        onNavigate = {
                            view.whichView.intValue = 0
                            view.onView.value = false
                            fields.inDeckClicked.value = false
                            navController.navigate(DeckViewDestination.createRoute(deckId ?: 0))
                            coroutineScope.launch {
                                /** This function also gets the due cards */
                                updateDecksCardList(
                                    it,
                                    cardsToUpdate,
                                    cardDeckVM
                                )
                            }
                        }
                    )
                }
            }

            composable(EditDeckDestination.route) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")!!.toIntOrNull()
                val currentName = backStackEntry.arguments?.getString("currentName")

                BackHandler {
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.inDeckClicked.value = false
                    navController.popBackStack(
                        DeckViewDestination.createRoute(deckId ?: 0),
                        inclusive = false
                    )
                }
                deck.value?.let {
                    editDeckView.EditDeck(
                        currentName = currentName ?: "",
                        deck = it,
                        onNavigate = {
                            view.whichView.intValue = 0
                            view.onView.value = false
                            fields.inDeckClicked.value = false
                            navController.navigate(DeckViewDestination.createRoute(deckId ?: 0))
                        },
                        onDelete = {
                            view.whichView.intValue = 0
                            view.onView.value = false
                            fields.inDeckClicked.value = false
                            navController.navigate(DeckListDestination.route)
                        }
                    )
                }
            }

            composable(ViewAllCardsDestination.route) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")!!.toIntOrNull()
                BackHandler {
                    view.whichView.intValue = 0
                    view.onView.value = false
                    fields.inDeckClicked.value = false
                    navController.popBackStack(
                        DeckViewDestination.createRoute(deckId ?: 0),
                        inclusive = false
                    )
                }
                deck.value?.let {
                    deckEditView.ViewFlashCards(
                        deck = it,
                        onNavigate = {
                            view.whichView.intValue = 0
                            fields.inDeckClicked.value = false
                            navController.navigate(DeckViewDestination.createRoute(deckId ?: 0))
                        },
                        goToEditCard = {
                            navController.navigate(
                                EditingCardDestination.createRoute(it.id)
                            )
                        }
                    )
                }
            }
            composable(
                route = EditingCardDestination.route,
                arguments = listOf(navArgument("deckId") { type = NavType.IntType })
            ) { backStackEntry ->
                //val cardId = backStackEntry.arguments?.getString("cardId")!!.toIntOrNull()
                //val deck = uiState.deckList.find { it.id == deckId }
                val deckId = backStackEntry.arguments?.getInt("deckId")

                BackHandler {
                    selectedCard.value = null
                    view.whichView.intValue = 0
                    deckEditView.isEditing.value = false
                    fields.inDeckClicked.value = false
                    fields.resetFields()
                    navController.popBackStack(
                        ViewAllCardsDestination.createRoute(deckId ?: 0),
                        inclusive = false
                    )
                }
                selectedCard.value?.let {
                    editingCardView.EditFlashCardView(
                        card = it,
                        fields = fields,
                        selectedCard = selectedCard,
                        onNavigateBack = {
                            selectedCard.value = null
                            view.whichView.intValue = 0
                            deckEditView.isEditing.value = false
                            fields.inDeckClicked.value = false
                            fields.resetFields()
                            navController.navigate(
                                ViewAllCardsDestination.createRoute(deckId ?: 0)
                            )
                        }
                    )
                }
            }
        }
    }
}
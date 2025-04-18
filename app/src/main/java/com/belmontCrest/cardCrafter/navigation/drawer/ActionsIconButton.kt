package com.belmontCrest.cardCrafter.navigation.drawer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.belmontCrest.cardCrafter.navigation.destinations.AddCardDestination
import com.belmontCrest.cardCrafter.navigation.destinations.DeckNavDestination
import com.belmontCrest.cardCrafter.navigation.destinations.DeckViewDestination
import com.belmontCrest.cardCrafter.navigation.destinations.EditDeckDestination
import com.belmontCrest.cardCrafter.navigation.destinations.EditingCardDestination
import com.belmontCrest.cardCrafter.navigation.NavViewModel
import com.belmontCrest.cardCrafter.navigation.destinations.SBNavDestination
import com.belmontCrest.cardCrafter.navigation.destinations.ViewAllCardsDestination
import com.belmontCrest.cardCrafter.navigation.destinations.ViewDueCardsDestination
import com.belmontCrest.cardCrafter.navigation.destinations.SupabaseDestination
import com.belmontCrest.cardCrafter.controller.viewModels.cardViewsModels.CardDeckViewModel
import com.belmontCrest.cardCrafter.model.uiModels.Fields
import com.belmontCrest.cardCrafter.supabase.controller.viewModels.SupabaseViewModel
import com.belmontCrest.cardCrafter.ui.theme.GetUIStyle
import com.belmontCrest.cardCrafter.ui.theme.backButtonModifier
import com.belmontCrest.cardCrafter.ui.theme.redoButtonModifier
import com.belmontCrest.cardCrafter.ui.theme.settingsButtonModifier
import com.belmontCrest.cardCrafter.uiFunctions.BackButton
import com.belmontCrest.cardCrafter.uiFunctions.CardOptionsButton
import com.belmontCrest.cardCrafter.uiFunctions.CardTypesButton
import com.belmontCrest.cardCrafter.uiFunctions.EditProfileButton
import com.belmontCrest.cardCrafter.uiFunctions.RedoCardButton
import com.belmontCrest.cardCrafter.uiFunctions.SettingsButton
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ActionIconButton(
    getUIStyle: GetUIStyle,
    cardDeckVM: CardDeckViewModel,
    fields: Fields,
    navViewModel: NavViewModel,
    supabaseVM: SupabaseViewModel
) {
    val deckNavController by navViewModel.deckNav.collectAsStateWithLifecycle()
    val sc by navViewModel.card.collectAsStateWithLifecycle()
    val wd by navViewModel.wd.collectAsStateWithLifecycle()
    val user by supabaseVM.currentUser.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val onNavigateBack: () -> Unit = {
        fields.isEditing.value = false
        fields.inDeckClicked.value = false
        coroutineScope.launch {
            navViewModel.getCardById(0)
        }
        navViewModel.updateRoute(ViewAllCardsDestination.route)
        deckNavController?.navigate(ViewAllCardsDestination.route)
    }
    val cr = navViewModel.route.collectAsStateWithLifecycle().value


    when (cr.name) {
        SupabaseDestination.route -> {
            if (user != null) {
                EditProfileButton(getUIStyle, navViewModel, supabaseVM)
            }
        }

        SBNavDestination.route -> {
            if (user != null) {
                EditProfileButton(getUIStyle, navViewModel, supabaseVM)
            }
        }

        DeckNavDestination.route -> {
            wd.deck?.let {
                SettingsButton(
                    onNavigateToEditDeck = {
                        if (!fields.inDeckClicked.value) {
                            fields.inDeckClicked.value = true
                            fields.mainClicked.value = false
                            navViewModel.updateRoute(
                                EditDeckDestination.createRoute(it.name)
                            )
                            deckNavController?.navigate(
                                EditDeckDestination.createRoute(it.name)
                            )
                        }
                    },
                    onNavigateToEditCards = {
                        if (!fields.inDeckClicked.value) {
                            fields.inDeckClicked.value = true
                            fields.mainClicked.value = false
                            navViewModel.updateRoute(ViewAllCardsDestination.route)
                            deckNavController?.navigate(ViewAllCardsDestination.route)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .settingsButtonModifier(),
                    getUIStyle = getUIStyle,
                    fields = fields
                )
            }
        }

        DeckViewDestination.route -> {
            wd.deck?.let {
                SettingsButton(
                    onNavigateToEditDeck = {
                        if (!fields.inDeckClicked.value) {
                            fields.inDeckClicked.value = true
                            fields.mainClicked.value = false
                            navViewModel.updateRoute(
                                EditDeckDestination.createRoute(it.name)
                            )
                            deckNavController?.navigate(
                                EditDeckDestination.createRoute(it.name)
                            )
                        }
                    },
                    onNavigateToEditCards = {
                        if (!fields.inDeckClicked.value) {
                            fields.inDeckClicked.value = true
                            fields.mainClicked.value = false
                            navViewModel.updateRoute(ViewAllCardsDestination.route)
                            deckNavController?.navigate(ViewAllCardsDestination.route)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .settingsButtonModifier(),
                    getUIStyle = getUIStyle,
                    fields = fields
                )
            }
        }

        ViewAllCardsDestination.route -> {
            BackButton(
                onBackClick = {
                    fields.inDeckClicked.value = false
                    navViewModel.updateRoute(DeckViewDestination.route)
                    deckNavController?.navigate(DeckViewDestination.route)
                },
                getUIStyle = getUIStyle,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .backButtonModifier()
            )
        }

        AddCardDestination.route -> {
            CardTypesButton(getUIStyle, navViewModel)
        }

        ViewDueCardsDestination.route -> {
            RedoCardButton(
                onRedoClick = {
                    cardDeckVM.updateRedoClicked(true)
                },
                modifier = Modifier
                    .redoButtonModifier(),
                getUIStyle = getUIStyle
            )
        }

        EditingCardDestination.route -> {
            val expanded = rememberSaveable { mutableStateOf(false) }
            sc.card?.let {
                CardOptionsButton(
                    navViewModel, getUIStyle, it, fields,
                    expanded, Modifier, onNavigateBack
                )
            }
        }
    }
}

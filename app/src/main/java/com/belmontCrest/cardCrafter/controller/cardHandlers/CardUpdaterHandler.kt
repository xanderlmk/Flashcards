package com.belmontCrest.cardCrafter.controller.cardHandlers

import com.belmontCrest.cardCrafter.controller.viewModels.cardViewsModels.CardDeckViewModel
import com.belmontCrest.cardCrafter.model.repositories.FlashCardRepository
import com.belmontCrest.cardCrafter.model.tablesAndApplication.Card
import com.belmontCrest.cardCrafter.model.tablesAndApplication.Deck
import com.belmontCrest.cardCrafter.model.tablesAndApplication.SavedCard
import com.belmontCrest.cardCrafter.model.uiModels.CardState
import java.util.Calendar
import java.util.Date


fun updateCard(
    card: Card, isSuccess: Boolean,
    deckGoodMultiplier: Double,
    deckBadMultiplier: Double, deckReviewAmount: Int,
    again: Boolean
): Card {
    val temp = card
    if (temp.reviewsLeft <= 1) {
        if (isSuccess) {
            temp.passes += 1
            temp.prevSuccess = true
            temp.reviewsLeft = deckReviewAmount
        } else {
            if (!again) {
                temp.reviewsLeft = deckReviewAmount
            }
            temp.prevSuccess = false
        }
        temp.nextReview = timeCalculator(
            temp.passes, isSuccess,
            deckGoodMultiplier, deckBadMultiplier
        )

        if (!isSuccess && !temp.prevSuccess && temp.passes > 0) {
            temp.passes -= 1
        }
    } else {
        /** When the user reviews a card x amount of times
         *  Default value is 1
         */
        if (isSuccess) {
            temp.reviewsLeft -= 1
        }
    }
    temp.totalPasses += 1
    temp.partOfList = true
    return temp
}

fun timeCalculator(
    passes: Int, isSuccess: Boolean,
    deckGoodMultiplier: Double,
    deckBadMultiplier: Double
): Date {
    val calendar = Calendar.getInstance()
    // Determine the multiplier based on success or hard pass
    val multiplier =
        calculateReviewMultiplier(
            passes, isSuccess,
            deckGoodMultiplier, deckBadMultiplier
        )
    // Calculate days to add
    val daysToAdd = (passes * multiplier).toInt()
    // Add days to the current date
    calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)

    // Return the updated date
    return calendar.time
}

private fun calculateReviewMultiplier(
    passes: Int, isSuccess: Boolean,
    deckGoodMultiplier: Double,
    deckBadMultiplier: Double
): Double {
    val baseMultiplier = when {
        passes == 1 -> 1.0
        passes >= 2 -> deckBadMultiplier
        else -> 0.0
    }
    return if (isSuccess) deckGoodMultiplier else baseMultiplier
}

fun handleCardUpdate(
    card: Card, success: Boolean,
    viewModel: CardDeckViewModel,
    deckGoodMultiplier: Double, deckBadMultiplier: Double,
    deckReviewAmount: Int, again: Boolean
): Card {
    return updateCard(
        card, success, deckGoodMultiplier,
        deckBadMultiplier, deckReviewAmount,
        again
    ).also {
        viewModel.transitionTo(CardState.Finished)
        viewModel.addCardToUpdate(it)
    }
}

suspend fun updateDecksCardList(
    deck: Deck,
    cardList: List<Card>,
    cardDeckViewModel: CardDeckViewModel,
): Boolean {
    return cardDeckViewModel.updateCards(deck, cardList)
}

suspend fun fcrUpdateCard(flashCardRepository: FlashCardRepository, card: Card) {
    flashCardRepository.updateCard(
        Card(
            id = card.id,
            deckId = card.deckId,
            deckUUID = card.deckUUID,
            reviewsLeft = card.reviewsLeft,
            nextReview = card.nextReview,
            passes = card.passes,
            prevSuccess = card.prevSuccess,
            totalPasses = card.totalPasses,
            type = card.type,
            deckCardNumber = card.deckCardNumber,
            cardIdentifier = card.cardIdentifier,
            createdOn = card.createdOn,
            partOfList = false,
        )
    )
}

fun returnSavedCard(card: Card) : SavedCard {
    return SavedCard(
        id = card.id,
        reviewsLeft = card.reviewsLeft,
        nextReview = card.nextReview,
        prevSuccess = card.prevSuccess,
        passes = card.passes,
        totalPasses = card.totalPasses,
        partOfList = card.partOfList
    )
}
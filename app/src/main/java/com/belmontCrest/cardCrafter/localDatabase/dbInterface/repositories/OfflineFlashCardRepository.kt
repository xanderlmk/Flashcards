package com.belmontCrest.cardCrafter.localDatabase.dbInterface.repositories

import com.belmontCrest.cardCrafter.localDatabase.dbInterface.daoInterfaces.deckAndCardDao.CardDao
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.daoInterfaces.deckAndCardDao.DeckDao
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.daoInterfaces.deckAndCardDao.DeckId
import com.belmontCrest.cardCrafter.localDatabase.dbInterface.daoInterfaces.deckAndCardDao.SavedCardDao
import com.belmontCrest.cardCrafter.localDatabase.tables.Card
import com.belmontCrest.cardCrafter.localDatabase.tables.Deck
import com.belmontCrest.cardCrafter.localDatabase.tables.SavedCard
import com.belmontCrest.cardCrafter.views.miscFunctions.details.CDetails
import kotlinx.coroutines.flow.Flow
import java.util.Date

class OfflineFlashCardRepository(
    private val deckDao: DeckDao,
    private val cardDao: CardDao,
    private val savedCardDao: SavedCardDao
) : FlashCardRepository {

    override suspend fun checkIfDeckExists(deckName: String): Int =
        try {
            deckDao.checkIfDeckExists(deckName)
        } catch (e: Exception) {
            throw e
        }

    override suspend fun checkIfDeckExists(deckName: String, deckUUID: String): Int =
        try {
            deckDao.checkIfDeckExists(deckName, deckUUID)
        } catch (e: Exception) {
            throw e
        }

    override suspend fun checkIfDeckUUIDExists(deckUUID: String): Int =
        try {
            deckDao.checkIfDeckUUIDExists(deckUUID)
        } catch (e: Exception) {
            throw e
        }

    override fun getAllDecksStream(): Flow<List<Deck>> = deckDao.getAllDecksStream()

    override suspend fun getAllDecks() = deckDao.getAllDecks()

    override fun getDeckStream(id: Int) = deckDao.getDeckFlow(id)

    override fun getDeck(id: Int) = deckDao.getDeck(id)

    override fun getDeckName(id: Int) = deckDao.getDeckName(id)

    override fun getCardCount(currentTime: Long) = deckDao.getCardCount(currentTime)

    override fun resetCardLefts() = deckDao.resetCardLefts()

    override suspend fun insertDeck(deck: Deck) = deckDao.insertDeck(deck)

    override suspend fun deleteDeck(deck: Deck) = deckDao.deleteDeck(deck)
    override suspend fun deleteDeckById(id: Int) = deckDao.deleteDeckById(DeckId(id))

    override suspend fun updateDeck(deck: Deck) = deckDao.updateDeck(deck)

    override suspend fun updateDeckName(newName: String, deckID: Int): Int =
        try {
            deckDao.updateDeckName(newName, deckID)
        } catch (e: Exception) {
            throw e
        }

    override fun updateDeckGoodMultiplier(newMultiplier: Double, deckId: Int) =
        try {
            deckDao.updateDeckGoodMultiplier(newMultiplier, deckId)
        } catch (e: Exception) {
            throw e
        }

    override fun updateDeckBadMultiplier(newMultiplier: Double, deckId: Int) =
        try {
            deckDao.updateDeckBadMultiplier(newMultiplier, deckId)
        } catch (e: Exception) {
            throw e
        }

    override fun updateDeckReviewAmount(newReviewAmount: Int, deckId: Int) =
        try {
            deckDao.updateReviewAmount(newReviewAmount, deckId)
        } catch (e: Exception) {
            throw (e)
        }

    override fun updateCardReviewAmount(newReviewAmount: Int, deckId: Int) =
        try {
            cardDao.updateReviewAmount(newReviewAmount, deckId)
        } catch (e: Exception) {
            throw (e)
        }

    override suspend fun updateNextReview(nextReview: Date, deckId: Int) =
        try {
            deckDao.updateNextReview(nextReview, deckId)
        } catch (e: Exception) {
            throw (e)
        }

    override fun updateCardAmount(cardAmount: Int, deckId: Int) =
        try {
            deckDao.updateCardAmount(cardAmount, deckId)
        } catch (e: Exception) {
            throw (e)
        }

    override suspend fun insertCard(card: Card) = cardDao.insertCard(card)

    override suspend fun getMaxDCNumber(deckUUID: String) = cardDao.getMaxDCNumber(deckUUID)

    override suspend fun updateCard(card: Card) = cardDao.updateCard(card)

    override suspend fun deleteCard(card: Card) = cardDao.deleteCard(card)

    override fun getCardStream(cardId: Int) = cardDao.getCardStream(cardId)

    override suspend fun getDueCards(deckId: Int, currentTime: Long):
            Flow<List<Card>> = cardDao.getDueCards(deckId, currentTime)

    override suspend fun deleteAllCards(deckId: Int) = cardDao.deleteAllCards(deckId)

    override fun getCardById(cardId: Int) = cardDao.getCardById(cardId)

    override suspend fun getBackupDueCards(deckId: Int, cardAmount: Int): List<Card> =
        cardDao.getBackupDueCards(deckId, cardAmount)

    override fun updateSavedCards(
        cardId: Int,
        reviewsLeft: Int,
        nextReview: Long,
        passes: Int,
        prevSuccess: Boolean,
        totalPasses: Int,
        partOfList: Boolean
    ) = savedCardDao.updateCardsOnStart(
        cardId, reviewsLeft, nextReview,
        passes, prevSuccess, totalPasses, partOfList
    )

    override fun deleteSavedCards() = savedCardDao.deleteSavedCards()
    override fun insertSavedCard(savedCard: SavedCard) = savedCardDao.insertSavedCard(savedCard)
    override fun getAllSavedCards() = savedCardDao.getAllSavedCards()

    override suspend fun becomePartOfList(id: Int) = cardDao.becomePartOfList(id)

    override fun updateCardsLeft(deckId: Int, cardsLeft: Int, cardsDone: Int) =
        deckDao.updateCardsLeft(deckId, cardsLeft, cardsDone)

    override fun getDueDeckDetails(id: Int) = deckDao.getDueDeckDetails(id)

    override suspend fun insertBasicCard(
        deck: Deck, basicCD: CDetails.BasicCD, isOwnerOrCoOwner: Boolean
    ) = cardDao.insertBasicCard(deck, basicCD, isOwnerOrCoOwner)

    override suspend fun insertThreeCard(
        deck: Deck, threeCD: CDetails.ThreeCD, isOwnerOrCoOwner: Boolean
    ) = cardDao.insertThreeCard(deck, threeCD, isOwnerOrCoOwner)

    override suspend fun insertHintCard(
        deck: Deck, hintCD: CDetails.HintCD, isOwnerOrCoOwner: Boolean
    ) = cardDao.insertHintCard(deck, hintCD, isOwnerOrCoOwner)

    override suspend fun insertMultiCard(
        deck: Deck, multiCD: CDetails.MultiCD, isOwnerOrCoOwner: Boolean
    ) = cardDao.insertMultiCard(deck, multiCD, isOwnerOrCoOwner)

    override suspend fun insertNotationCard(
        deck: Deck, notationCD: CDetails.NotationCD, isOwnerOrCoOwner: Boolean
    ) = cardDao.insertNotationCard(deck, notationCD, isOwnerOrCoOwner)
}
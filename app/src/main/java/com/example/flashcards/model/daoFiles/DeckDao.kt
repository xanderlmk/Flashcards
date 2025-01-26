package com.example.flashcards.model.daoFiles

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.model.tablesAndApplication.Deck
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDeck(deck: Deck)

    @Update
    suspend fun updateDeck(deck: Deck)

    @Delete
    suspend fun deleteDeck(deck: Deck)

    @Query("SELECT * from decks WHERE id = :id")
    fun getDeck(id: Int): Flow<Deck>

    @Query("SELECT * from decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<Deck>>


    @Query(
        """WITH RankedCards AS (
    SELECT
        c.deckId,
        COUNT(*) AS cardCount,
        d.cardsLeft
    FROM cards c
    INNER JOIN decks d ON c.deckId = d.id
    WHERE c.nextReview <= :currentTime
    AND d.nextReview <= :currentTime
    GROUP BY c.deckId, d.cardsLeft
    ORDER BY c.nextReview ASC 
    )
    SELECT 
    COALESCE(
        CASE 
            WHEN rc.cardCount > d.cardsLeft THEN d.cardsLeft
            ELSE rc.cardCount
        END, 0
    ) AS limitedCardCount
    FROM decks d
    LEFT JOIN RankedCards rc ON d.id = rc.deckId"""
    )
    fun getCardCount(currentTime: Long = Date().time): Flow<List<Int>>

    @Query(
        """
    WITH RankedCards AS (
    SELECT
        c.deckId,
        COUNT(*) AS cardCount,
        d.cardAmount,
        d.cardsLeft
    FROM cards c
    INNER JOIN decks d ON c.deckId = d.id
    WHERE c.nextReview <= :currentTime 
      AND d.nextReview <= :currentTime
    GROUP BY c.deckId, d.cardAmount, d.cardsLeft
    ORDER BY c.nextReview ASC 
    )
    UPDATE decks
    SET cardsLeft = (
        SELECT 
            CASE 
                WHEN rc.cardCount IS NULL THEN 0
                WHEN rc.cardCount > decks.cardAmount THEN decks.cardAmount
                ELSE rc.cardCount
        END
    FROM RankedCards rc
    WHERE rc.deckId = decks.id
    )
    WHERE EXISTS (
    SELECT 1
    FROM RankedCards rc
    WHERE rc.deckId = decks.id
    )
    AND NOT EXISTS (
        SELECT 1 
        FROM cards c
        WHERE c.partOfList = 1 AND c.deckId = decks.id
    )
    """
    )
    fun resetCardLefts(currentTime: Long = Date().time)

    @Query("SELECT COUNT(*) FROM decks WHERE LOWER(name) = LOWER(:deckName)")
    fun checkIfDeckExists(deckName: String): Int

    @Query(
        """
        UPDATE decks 
        SET name = :newName 
        WHERE id = :deckId
        AND NOT EXISTS (
            SELECT 1 
            FROM decks 
            WHERE LOWER(name) = LOWER(:newName) 
            AND id != :deckId
        )
    """
    )
    fun updateDeckName(newName: String, deckId: Int): Int

    @Query(
        """
        update decks
        set goodMultiplier = :newMultiplier
        where id = :deckId
        and :newMultiplier > 1.0
    """
    )
    fun updateDeckGoodMultiplier(newMultiplier: Double, deckId: Int): Int

    @Query(
        """
        update decks
        set badMultiplier = :newMultiplier
        where id = :deckId
        and :newMultiplier < 1.0
        and :newMultiplier > 0.0
    """
    )
    fun updateDeckBadMultiplier(newMultiplier: Double, deckId: Int): Int

    @Query(
        """
        update decks
        set reviewAmount = :newReviewAmount
        where id = :deckId
    """
    )
    fun updateReviewAmount(newReviewAmount: Int, deckId: Int): Int

    @Query(
        """
        update decks
        set nextReview = :nextReview
        where id = :deckId
    """
    )
    fun updateNextReview(nextReview: Date, deckId: Int)

    @Query(""" 
        UPDATE decks 
        SET cardsLeft = :cardsLeft
        WHERE id = :deckId
        AND cardsLeft > 0
    """)
    fun updateCardsLeft(deckId: Int, cardsLeft : Int)
}
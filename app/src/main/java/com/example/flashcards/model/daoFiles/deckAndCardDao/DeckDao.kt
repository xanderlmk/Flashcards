package com.example.flashcards.model.daoFiles.deckAndCardDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.model.tablesAndApplication.Deck
import com.example.flashcards.model.uiModels.DueDeckDetails
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
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
    fun getDeckFlow(id: Int): Flow<Deck>

    @Query("SELECT * from decks where id = :id")
    fun getDeck(id: Int) : Deck

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

    @Query("""
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
    ),
    lastUpdated = :startOfDay
    WHERE EXISTS (
    SELECT 1
    FROM RankedCards rc
    WHERE rc.deckId = decks.id
    )
    AND NOT EXISTS (
        SELECT 1 
        FROM cards c
        WHERE c.deckId = decks.id
        AND c.partOfList = 1
        AND decks.lastUpdated == :startOfDay
        )
    """
    )
    fun resetCardLefts(
        currentTime: Long = Date().time,
                       startOfDay : Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    )

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

    @Query("""
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
        ORDER BY c.partOfList DESC, c.nextReview ASC 
    )
        UPDATE decks
        SET cardAmount = :cardAmount,
        cardsLeft =  (
            SELECT
                CASE
                    WHEN rc.cardCount IS NULL THEN 0
                    WHEN cardsLeft >= :cardAmount then :cardAmount
                    ELSE cardsLeft
                END
            FROM RankedCards rc
            WHERE rc.deckId = :deckId
        )
        WHERE id = :deckId 
        AND cardAmount > 4
        AND cardAmount < 1001
    """)
    fun updateCardAmount(cardAmount : Int, deckId: Int, currentTime: Long = Date().time) : Int

    @Query("""
        SELECT id, cardsLeft, nextReview
        FROM decks WHERE id = :id
        """)
    fun getDueDeckDetails(id: Int): Flow<DueDeckDetails>
}
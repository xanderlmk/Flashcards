package com.example.flashcards.model.daoFiles

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.model.tablesAndApplication.Deck
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT COUNT(*) FROM decks WHERE LOWER(name) = LOWER(:deckName)")
    fun checkIfDeckExists(deckName: String): Int

    @Query("""
        UPDATE decks 
        SET name = :newName 
        WHERE id = :deckID 
        AND NOT EXISTS (
            SELECT 1 
            FROM decks 
            WHERE LOWER(name) = LOWER(:newName) 
            AND id != :deckID
        )
    """)
    fun updateDeckName(newName: String, deckID: Int): Int
}
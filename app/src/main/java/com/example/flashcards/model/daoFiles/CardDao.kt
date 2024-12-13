package com.example.flashcards.model.daoFiles

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.flashcards.model.tablesAndApplication.BasicCardType
import com.example.flashcards.model.tablesAndApplication.DeckWithCards
import com.example.flashcards.model.tablesAndApplication.Card
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCard(card: Card) : Long

    @Update
    suspend fun updateCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Transaction
    @Query("SELECT * FROM decks WHERE id = :deckId")
    fun getDeckWithCards(deckId: Int): Flow<DeckWithCards>

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND nextReview <= :currentTime")
    fun getDueCards(deckId: Int, currentTime: Long = Date().time): Flow<List<Card>>

    @Query("DELETE FROM cards WHERE deckId = :deckId")
    suspend fun deleteAllCards(deckId: Int)

    @Query("Update cards set id = :cardId and type = :type")
    suspend fun updateCard(cardId: Int, type: String)

    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId : Int) : Card?

}
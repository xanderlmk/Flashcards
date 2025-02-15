package com.example.flashcards.model.daoFiles.allCardTypesDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcards.model.tablesAndApplication.ThreeFieldCard

@Dao
interface ThreeCardDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertThreeCard(threeFieldCard: ThreeFieldCard) : Long


    @Query("DELETE FROM threeFieldCard WHERE cardId = :cardId")
    suspend fun deleteThreeCard(cardId: Int)

    @Query("""
        Update threeFieldCard
        Set question = :newQuestion, 
        middle = :newMiddle,
        answer = :newAnswer
        where cardId = :id""")
    suspend fun updateThreeCard(
        id: Int,
        newQuestion: String,
        newMiddle: String,
        newAnswer: String
    )

}
package com.belmontCrest.cardCrafter.model.repositories


import com.belmontCrest.cardCrafter.model.tablesAndApplication.AllCardTypes
import com.belmontCrest.cardCrafter.model.tablesAndApplication.BasicCard
import com.belmontCrest.cardCrafter.model.tablesAndApplication.CT
import com.belmontCrest.cardCrafter.model.tablesAndApplication.HintCard
import com.belmontCrest.cardCrafter.model.tablesAndApplication.MultiChoiceCard
import com.belmontCrest.cardCrafter.model.tablesAndApplication.ThreeFieldCard
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface CardTypeRepository {
    suspend fun insertBasicCard(basicCard: BasicCard): Long
    suspend fun insertThreeCard(threeFieldCard: ThreeFieldCard): Long
    suspend fun insertHintCard(hintCard: HintCard): Long
    suspend fun insertMultiChoiceCard(multiChoiceCard: MultiChoiceCard): Long

    suspend fun deleteBasicCard(basicCard: BasicCard)
    suspend fun deleteThreeCard(threeFieldCard: ThreeFieldCard)
    suspend fun deleteHintCard(hintCard: HintCard)
    suspend fun deleteMultiChoiceCard(multiChoiceCard: MultiChoiceCard)

    suspend fun updateBasicCard(id: Int, question: String, answer: String)

    suspend fun updateThreeCard(
        id: Int, question: String, middle: String,
        answer: String
    )

    suspend fun updateHintCard(
        id: Int, question: String, hint: String,
        answer: String
    )

    suspend fun updateMultiChoiceCard(
        id: Int,
        newQuestion: String,
        newChoiceA: String,
        newChoiceB: String,
        newChoiceC: String,
        newChoiceD: String,
        newCorrect: Char
    )

    fun getAllCardTypes(deckId: Int): Flow<List<AllCardTypes>>

    fun getAllDueCards(
        deckId: Int,
        cardAmount: Int,
        currentTime: Long
    ): Flow<List<CT>>

    fun getDueAllCardTypes(deckId: Int, cardAmount: Int, currentTime: Long = Date().time):
            List<CT>

    fun getACardType(id: Int): AllCardTypes
}
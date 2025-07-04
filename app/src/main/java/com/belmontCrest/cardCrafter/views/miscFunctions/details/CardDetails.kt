package com.belmontCrest.cardCrafter.views.miscFunctions.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.belmontCrest.cardCrafter.localDatabase.tables.CT
import com.belmontCrest.cardCrafter.localDatabase.tables.ListStringConverter
import com.belmontCrest.cardCrafter.localDatabase.tables.PartOfQorA
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsBasic
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsHint
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsMulti
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsNotation
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsThree
import com.belmontCrest.cardCrafter.supabase.model.tables.SBCardColsWithCT

data class CardDetails(
    val question: MutableState<String> = mutableStateOf(""),
    val middleField: MutableState<String> = mutableStateOf(""),
    val answer: MutableState<String> = mutableStateOf(""),
    val choices: MutableList<MutableState<String>> = MutableList(4) { mutableStateOf("") },
    val correct: MutableState<Char> = mutableStateOf('?'),
    val stringList: MutableList<MutableState<String>> = mutableListOf(),
    val isQorA: MutableState<PartOfQorA> = mutableStateOf(PartOfQorA.Q)
)

fun CT.toQuestion(): String = when (this) {
    is CT.Basic -> this.basicCard.question
    is CT.Hint -> this.hintCard.question
    is CT.MultiChoice -> this.multiChoiceCard.question
    is CT.Notation -> this.notationCard.question
    is CT.ThreeField -> this.threeFieldCard.question
}

private val listStringC = ListStringConverter()


fun SBCardColsWithCT.toCardDetails(): CardDetails = when (this) {
    is SBCardColsBasic -> CardDetails(
        question = mutableStateOf(basicCard.question),
        answer = mutableStateOf(basicCard.answer)
    )

    is SBCardColsThree -> CardDetails(
        question = mutableStateOf(threeCard.question),
        middleField = mutableStateOf(threeCard.middle),
        answer = mutableStateOf(threeCard.answer)
    )

    is SBCardColsHint -> CardDetails(
        question = mutableStateOf(hintCard.question),
        middleField = mutableStateOf(hintCard.hint),
        answer = mutableStateOf(hintCard.answer)
    )

    is SBCardColsMulti -> CardDetails(
        question = mutableStateOf(multiCard.question),
        choices = mutableListOf(
            mutableStateOf(multiCard.choiceA),
            mutableStateOf(multiCard.choiceB),
            mutableStateOf(multiCard.choiceC ?: ""),
            mutableStateOf(multiCard.choiceD ?: "")
        ),
        correct = mutableStateOf(multiCard.correct)
    )

    is SBCardColsNotation -> CardDetails(
        question = mutableStateOf(notationCard.question),
        answer = mutableStateOf(notationCard.answer),
        stringList = listStringC.fromString(notationCard.steps).map {
            val thisString = mutableStateOf(it)
            thisString
        }.toMutableList()
    )
}

fun CT.toCardDetails(): CardDetails = when (this) {
    is CT.Basic -> CardDetails(
        question = mutableStateOf(basicCard.question),
        answer = mutableStateOf(basicCard.answer)
    )

    is CT.Hint -> CardDetails(
        question = mutableStateOf(hintCard.question),
        answer = mutableStateOf(hintCard.answer),
        middleField = mutableStateOf(hintCard.hint)
    )

    is CT.ThreeField -> CardDetails(
        question = mutableStateOf(threeFieldCard.question),
        middleField = mutableStateOf(threeFieldCard.middle),
        answer = mutableStateOf(threeFieldCard.answer),
        isQorA = mutableStateOf(threeFieldCard.field)
    )

    is CT.MultiChoice -> CardDetails(
        question = mutableStateOf(multiChoiceCard.question),
        // only include non‑blank choices
        choices = mutableListOf(
            mutableStateOf(multiChoiceCard.choiceA),
            mutableStateOf(multiChoiceCard.choiceB),
            mutableStateOf(multiChoiceCard.choiceC),
            mutableStateOf(multiChoiceCard.choiceD)
        ),
        correct = mutableStateOf(multiChoiceCard.correct)
    )

    is CT.Notation -> CardDetails(
        question = mutableStateOf(notationCard.question),
        answer = mutableStateOf(notationCard.answer),
        stringList = notationCard.steps.map {
            val thisString = mutableStateOf(it)
            thisString
        }.toMutableList(),
    )
}

sealed class CDetails {
    data class BasicCD(val question: String, val answer: String) : CDetails()

    data class ThreeCD(
        val question: String, val middle: String, val answer: String, val isQOrA: PartOfQorA
    ) : CDetails()

    data class HintCD(
        val question: String, val middle: String, val answer: String
    ) : CDetails()

    data class MultiCD(
        val question: String, val choiceA: String,
        val choiceB: String, val choiceC: String = "",
        val choiceD: String = "", val correct: Char
    ) : CDetails()

    data class NotationCD(
        val question: String, val steps: List<String>, val answer: String
    ) : CDetails()
}

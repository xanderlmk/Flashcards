package com.example.flashcards.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

class Fields(
    var question : MutableState<String> = mutableStateOf(""),
    var middleField : MutableState<String> = mutableStateOf(""),
    var answer : MutableState<String> = mutableStateOf(""),
    var scrollPosition : MutableState<Int> = mutableIntStateOf(0),
    val mainClicked : MutableState<Boolean> = mutableStateOf(false),
    val inDeckClicked : MutableState<Boolean> = mutableStateOf(false),
)
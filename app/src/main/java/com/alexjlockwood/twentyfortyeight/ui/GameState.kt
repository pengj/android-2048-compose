package com.alexjlockwood.twentyfortyeight.ui

data class GameState(
    val currentScore: Int = 0,
    val bestScore: Int = 0,
    val moveCount: Int = 0,
    val isGameOver: Boolean = false,
    val isDebugOn: Boolean = false,
    val isVoiceOn: Boolean = false,
    val direction: String = ""
)

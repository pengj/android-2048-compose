package com.alexjlockwood.twentyfortyeight.ui.observer

import com.alexjlockwood.twentyfortyeight.domain.Direction

class VoiceDirectionMapper {

    fun mappingToDirection(input: String): Direction? {
        return when {
            input.contains("north", true) -> Direction.NORTH
            input.contains("up", true) -> Direction.NORTH
            input.contains("south", true) -> Direction.SOUTH
            input.contains("down", true) -> Direction.SOUTH
            input.contains("east", true) -> Direction.EAST
            input.contains("right", true) -> Direction.EAST
            input.contains("west", true) -> Direction.WEST
            input.contains("left", true) -> Direction.WEST
            else -> null
        }
    }

}
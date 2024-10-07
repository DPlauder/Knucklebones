package com.example.knucklebones

data class Player(private var name: String, private var isHuman: Boolean) {
    private val points: Int = 0
    private val diceBoard: List<List<Dice>>
        get() = listOf(
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0))
        )


}
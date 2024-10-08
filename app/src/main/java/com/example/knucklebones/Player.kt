package com.example.knucklebones

data class Player(private var name: String, private var isHuman: Boolean) {
    private val points: Int = 0
    private var diceBoard: MutableList<MutableList<Dice>> = mutableListOf(
        mutableListOf(Dice(1), Dice(1), Dice(1)),
        mutableListOf(Dice(0), Dice(0), Dice(0)),
        mutableListOf(Dice(0), Dice(0), Dice(0))
    )

    fun getDiceRow(rowIndex: Int): List<Dice> = diceBoard[rowIndex]

    fun setDice(rowIndex: Int, diceValue: Int) {
        val row = diceBoard[rowIndex]
        val index = row.indexOfFirst { it.value == 0 }
        if (index != -1) {
            row.removeAt(index)
            row.add(index, Dice(diceValue))
        }
    }

}
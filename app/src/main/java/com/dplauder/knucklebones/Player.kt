package com.dplauder.knucklebones

data class Player(var name: String, var isHuman: Boolean = true) {
    private var points: Int = 0
    private var diceBoard: MutableList<MutableList<Dice>> = mutableListOf(
        mutableListOf(Dice(0), Dice(0), Dice(0)),
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
    fun deleteDice(rowIndex: Int, diceValue: Int){
        val row = diceBoard[rowIndex]
        val initalSize = row.size

        row.removeAll{ it.value == diceValue}
        val removedSize = initalSize - row.size
        repeat(removedSize){
            row.add(Dice(0))
        }
    }

    fun getDiceBoard(): MutableList<MutableList<Dice>> {
        return diceBoard
    }
    fun getRowPoints(rowIndex: Int): Int {
        val row = diceBoard[rowIndex]

        // Count the occurrences of each value in the row
        val countByValue = row.groupingBy { it.value }.eachCount()

        // Sum the points, multiplying each die value by the count of that value
        return row.sumOf { die ->
            die.value * countByValue[die.value]!!
        }
    }
    fun reset() {
        points = 0
        diceBoard = mutableListOf(
            mutableListOf(Dice(0), Dice(0), Dice(0)),
            mutableListOf(Dice(0), Dice(0), Dice(0)),
            mutableListOf(Dice(0), Dice(0), Dice(0))
        )
    }


    // nur für Anzeige Kontrolle
    fun printDiceValues(diceBoard: MutableList<MutableList<Dice>>) {
        for (row in diceBoard) {
            for (dice in row) {
                print("${dice.value} ")
            }
            println()
        }
        println("\n")
    }
    fun checkAllDiceSet(): Boolean{
        return diceBoard.all { row -> row.all { dice -> dice.value > 0 } }
    }



}
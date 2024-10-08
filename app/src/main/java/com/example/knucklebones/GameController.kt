package com.example.knucklebones

import android.widget.GridLayout

class GameController {
    private lateinit var player: MutableList<Player>
    //private lateinit var player2 : Player
    val gameDice = Dice(0)
    private var currentPlayerIndex: Int = 0

    fun start(player1: Player, player2: Player){
        player.add(player1)
        player.add(player2)

    }
    fun roll(){
        gameDice.roll()
    }
    fun setCurrentPlayer(){
        currentPlayerIndex = currentPlayerIndex % player.size + 1
    }

    fun validateRow(rowIndex: Int): Boolean{
        val diceList = player[currentPlayerIndex].getDiceRow(rowIndex)
        for(dice in diceList){
            if(dice.value == 0){
                return true
            }
        }
        return false
    }
    fun setDiceInRow(rowIndex: Int){
        player[currentPlayerIndex].setDice(rowIndex, gameDice.value)
    }




}
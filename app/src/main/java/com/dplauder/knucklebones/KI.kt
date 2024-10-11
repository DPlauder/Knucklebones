package com.dplauder.knucklebones

class KI(val difficulty: Difficulty, private val gameController: GameController) {
    enum class Difficulty {
        EASY, MEDIUM, HARD
    }
    fun playTurn(){
        when(difficulty){
            Difficulty.EASY -> easyPlay()
            Difficulty.MEDIUM -> mediumPlay()
            Difficulty.HARD -> hardPlay()
        }
    }

    private fun easyPlay(){
        gameController.roll()
        var rowIndex = (0..2).random()
        while(!gameController.validateRow(rowIndex)){
            rowIndex = (0..2).random()
        }
        gameController.setDiceInRow(rowIndex)
        gameController.setNextPlayerTurn()
    }

    private fun mediumPlay(){
        gameController.roll()
        val diceValue = gameController.getDiceValue()
        val opponentPlayer = gameController.getOpponentPlayer()

        var selectedRow = -1
        //
        for(rowIndex in 0..2){
            if(opponentPlayer.getDiceRow(rowIndex).any {it.value == diceValue} && gameController.validateRow(rowIndex)){
                selectedRow = rowIndex
                break
            }
        }
        if(selectedRow == -1){
            selectedRow = (0..2).random()
            while(!gameController.validateRow(selectedRow)){
                selectedRow = (0..2).random()
            }
        }
        gameController.setDiceInRow(selectedRow)
        gameController.setNextPlayerTurn()
    }

    private fun hardPlay(){
        gameController.roll()
        val diceValue = gameController.getDiceValue()
        val opponentPlayer = gameController.getOpponentPlayer()
        var bestRow = -1
        var bestScore = -1

        for(rowIndex in 0..2){
            if(gameController.validateRow(rowIndex)){
                val potentialScore = gameController.getRowPoints(rowIndex, gameController.getCurrPlayerIndex())
                val canBlockOpponent = opponentPlayer.getDiceRow(rowIndex).any{it.value == diceValue}
                if(canBlockOpponent || potentialScore > bestScore){
                    bestScore = potentialScore
                    bestRow = rowIndex
                }
            }
            if(bestRow != -1){
                bestRow = (0..2).random()
                while(!gameController.validateRow(bestRow)){
                    bestRow = (0..2).random()
                }
            }
            gameController.setDiceInRow(bestRow)
            gameController.setNextPlayerTurn()
        }
    }
}
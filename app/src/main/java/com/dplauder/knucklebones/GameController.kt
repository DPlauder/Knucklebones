package com.dplauder.knucklebones

class GameController {
    private lateinit var player: MutableList<Player>
    private lateinit var gamePhase: Gamephase
    val gameDice = Dice(0)
    private var currentPlayerIndex: Int = 0

    fun start(player1: Player, player2: Player){
        player = mutableListOf()
        gamePhase = Gamephase.ROLL_DICE
        player.add(player1)
        player.add(player2)

    }
    // Setter-Funktionen
    fun setCurrentPlayer(){
        currentPlayerIndex = (currentPlayerIndex + 1) % player.size
    }

    // Getter-Funktionen
    fun getPlayers(): MutableList<Player>{
        return player
    }
    fun getCurrPlayerIndex(): Int = currentPlayerIndex

    fun getCurrentPlayer(): Player = player[currentPlayerIndex]

    fun getOpponentPlayer(): Player{
        val opponentIndex = (currentPlayerIndex + 1) % player.size
        return player[opponentIndex]
    }
    fun getDiceValue(): Int = gameDice.value
    fun getGamePhase(): Gamephase = gamePhase
    fun getRowPoints(rowIndex: Int, playerIndex: Int): Int{
        return player[playerIndex].getRowPoints(rowIndex)
    }

    //
    fun roll(){
        if(gamePhase == Gamephase.ROLL_DICE){
            gameDice.roll()
            gamePhase = Gamephase.CHOOSE_PART
        }

    }
    fun validateRow(rowIndex: Int): Boolean{
        if(gamePhase != Gamephase.CHOOSE_PART){
            return false
        }
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
        if(checkOpponentRow(rowIndex)){
            getOpponentPlayer().deleteDice(rowIndex, gameDice.value)
        }
    }

    private fun checkOpponentRow(rowIndex: Int): Boolean{
        val diceList = getOpponentPlayer().getDiceRow(rowIndex)
        for(dice in diceList) {
            if (dice.value == gameDice.value) {
                return true
            }
        }
        return false
    }
    fun setNextPlayerTurn(){
        player[currentPlayerIndex].printDiceValues(player[currentPlayerIndex].getDiceBoard())
        getOpponentPlayer().printDiceValues(getOpponentPlayer().getDiceBoard())
        if(checkGameEnd()){
            println("END GAME")
            setGameEnd()
            return
        } else{
            setCurrentPlayer()
            gamePhase = Gamephase.ROLL_DICE
        }

    }
    fun checkGameEnd(): Boolean{
        return player[currentPlayerIndex].checkAllDiceSet()
    }
    fun setGameEnd(){
        gamePhase = Gamephase.GAME_END

    }

}
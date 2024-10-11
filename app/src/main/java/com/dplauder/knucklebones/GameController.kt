package com.dplauder.knucklebones

class GameController {
    private lateinit var player: MutableList<Player>
    private lateinit var gamePhase: Gamephase
    val gameDice = Dice(0)
    private var currentPlayerIndex: Int = 0
    private lateinit var gameActivity: GameActivity

    private var ki: KI? = null

    //TODO Initialisierung
    fun start(player1: Player, player2: Player, ki: KI? = null, gameActivity: GameActivity){
        player = mutableListOf()
        gamePhase = Gamephase.ROLL_DICE
        player.add(player1)
        player.add(player2)
        this.ki = ki
        this.gameActivity = gameActivity

    }
    fun restart(){
        gamePhase = Gamephase.ROLL_DICE
        currentPlayerIndex = 0
        gameDice.reset()
        for(player in player){
            player.reset()
        }
    }

    //TODO Setter-Funktionen
    fun setCurrentPlayer(){
        currentPlayerIndex = (currentPlayerIndex + 1) % player.size
    }
    fun setGameEnd(){
        gamePhase = Gamephase.GAME_END

    }

    //TODO Getter-Funktionen
    fun getPlayers(): MutableList<Player>{
        return player
    }
    fun getWinner(): Int{
        if(player[0].getPoints() > player[1].getPoints()){
            return 0
        }
        else if(player[0].getPoints() < player[1].getPoints()){
            return 1
        }
        else{
            return -1
        }
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
    fun getPlayerPoints(playerIndex: Int): Int{
        return player[playerIndex].getPoints()
    }

    //TODO Game Logic
    fun roll(){
        if(gamePhase == Gamephase.ROLL_DICE){
            gameDice.roll()
            gamePhase = Gamephase.CHOOSE_PART
        }

    }

    fun setDiceInRow(rowIndex: Int){
        player[currentPlayerIndex].setDice(rowIndex, gameDice.value)
        if(checkOpponentRow(rowIndex)){
            getOpponentPlayer().deleteDice(rowIndex, gameDice.value)
        }
    }

    fun setNextPlayerTurn(){
        player[currentPlayerIndex].printDiceValues(player[currentPlayerIndex].getDiceBoard())
        getOpponentPlayer().printDiceValues(getOpponentPlayer().getDiceBoard())


        if(checkGameEnd()){
            println("END GAME")
            setGameEnd()
            return
        }
        setCurrentPlayer()
        gamePhase = Gamephase.ROLL_DICE

        val currentPlayer = getCurrentPlayer()
        if(!currentPlayer.isHuman){
            ki?.playTurn()

        }
        gameActivity.updateDiceBoard()

    }
    //TODO checks & Validations
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

    private fun checkOpponentRow(rowIndex: Int): Boolean{
        val diceList = getOpponentPlayer().getDiceRow(rowIndex)
        for(dice in diceList) {
            if (dice.value == gameDice.value) {
                return true
            }
        }
        return false
    }
    fun checkGameEnd(): Boolean{
        return player[currentPlayerIndex].checkAllDiceSet()
    }


}
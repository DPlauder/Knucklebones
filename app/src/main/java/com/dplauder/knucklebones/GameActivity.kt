package com.dplauder.knucklebones

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout

import android.content.Intent
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import com.dplauder.knucklebones.databinding.ActivityGameBinding
import com.dplauder.knucklebones.databinding.DiceRowPlayer1Binding
import com.dplauder.knucklebones.databinding.DiceRowPlayer2Binding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameController: GameController
    private lateinit var dice_roll_section: LinearLayout
    private lateinit var rolledDiceImage: ImageView
    private var ki: KI? = null
    private lateinit var playerOneDiceRowBindings: List<DiceRowPlayer1Binding>
    private lateinit var playerTwoDiceRowBindings: List<DiceRowPlayer2Binding>
    private val diceImages = listOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        val playerName1 = intent.getStringExtra("PLAYER_1_NAME")
        val playerName2 = intent.getStringExtra("PLAYER_2_NAME")

        val player2Type = intent.getStringExtra("PLAYER_2_TYPE")
        val difficulty = intent.getStringExtra("DIFFICULTY")

        val player1 = Player(playerName1.toString())
        var player2 = Player(playerName2.toString())

        gameController = GameController()

        if(player2Type == "Human"){
            player2 = Player(playerName2.toString())
        }else{
            player2.isHuman = false
            val kiDifficulty = when(difficulty){
                "EASY" -> KI.Difficulty.EASY
                "MEDIUM" -> KI.Difficulty.MEDIUM
                "HARD" -> KI.Difficulty.HARD
                else -> KI.Difficulty.EASY
            }
            println("hello kidiff" + kiDifficulty)
            ki = KI(kiDifficulty, gameController)
        }

        gameController.start(player1, player2, ki, this)

        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playerOneName.text = player1.name
        binding.playerTwoName.text = player2.name

        playerOneDiceRowBindings = listOf(
            binding.diceRowPlayer11,
            binding.diceRowPlayer12,
            binding.diceRowPlayer13
        )
        playerTwoDiceRowBindings = listOf(
            binding.diceRowPlayer21,
            binding.diceRowPlayer22,
            binding.diceRowPlayer23
        )
        //RULES BTN
        binding.infoButton.setOnClickListener {
            val rulesDialog = RulesDialogFragment()
            rulesDialog.show(supportFragmentManager, "RulesDialog")
        }
        // HOME BTN
        binding.homeButton.setOnClickListener {
            showConfirmationDialog()
        }


        setRolledDiceClickListener()
        setupDiceRowPlayerOneClickListener(playerOneDiceRowBindings)
        setupDiceRowPlayertwoClickListener(playerTwoDiceRowBindings)
        updatePoints()
    }

    private fun setupDiceRowPlayerOneClickListener(rows: List<DiceRowPlayer1Binding>) {
        for (i in rows.indices) {
            val rowBinding = rows[i]
            rowBinding.root.setOnClickListener {
                if (gameController.getGamePhase() == Gamephase.CHOOSE_PART && gameController.getCurrPlayerIndex() == 0) {
                    println("Hello Row Click")
                    if (gameController.validateRow(i)) {
                        gameController.setDiceInRow(i)
                        updateDiceRowPlayer1(i)
                        updateDiceRowPlayer2(i)
                        gameController.setNextPlayerTurn()
                        if(gameController.getGamePhase() == Gamephase.GAME_END){
                            showGameEndDialog()
                        }

                    }
                }

            }
        }
    }
    private fun setupDiceRowPlayertwoClickListener(rows: List<DiceRowPlayer2Binding>) {
        for (i in rows.indices) {
            val rowBinding = rows[i]
            rowBinding.root.setOnClickListener {
                if (gameController.getGamePhase() == Gamephase.CHOOSE_PART && gameController.getCurrPlayerIndex() == 1) {
                    if (gameController.validateRow(i)) {
                        gameController.setDiceInRow(i)
                        updateDiceRowPlayer1(i)
                        updateDiceRowPlayer2(i)
                        gameController.setNextPlayerTurn()
                        if(gameController.getGamePhase() == Gamephase.GAME_END){
                            showGameEndDialog()
                        }
                    }
                }
            }
        }
    }
    private fun setRolledDiceClickListener() {
        rolledDiceImage = binding.rolledDiceImage
        dice_roll_section = binding.diceRollSection

        dice_roll_section.setOnClickListener{
            if(gameController.getGamePhase() == Gamephase.ROLL_DICE){
                rollDiceWithAnimation()
            }

        }
    }

    private fun rollDiceWithAnimation() {
        val handler = Handler(Looper.getMainLooper())
        val rollDuration = 1000L // total duration for the roll animation in milliseconds
        val interval = 100L // interval for switching dice images
        var elapsed = 0L

        // Simulate dice rolling by changing the image every 100ms
        val runnable = object : Runnable {
            override fun run() {
                if (elapsed < rollDuration) {
                    val randomIndex = Random.nextInt(6) // random dice face
                    rolledDiceImage.setImageResource(diceImages[randomIndex])
                    elapsed += interval
                    handler.postDelayed(this, interval)
                } else {
                    // Set the final dice value after rolling
                    gameController.roll()
                    val finalDiceValue = gameController.gameDice.value
                    rolledDiceImage.setImageResource(getDiceImageResId(finalDiceValue))
                }
            }
        }
        handler.post(runnable)
    }

    fun updateDiceBoard(){
        for (rowIndex in 0 until 3) {
            updateDiceRowPlayer1(rowIndex)
            updateDiceRowPlayer2(rowIndex)
        }
        updatePoints()
    }
    fun updatePoints(){
        binding.playerOnePoints.text = gameController.getPlayerPoints(0).toString()
        binding.playerTwoPoints.text = gameController.getPlayerPoints(1).toString()
    }

    private fun updateDiceRowPlayer1(rowIndex: Int) {
        val diceList = gameController.getPlayers()[0].getDiceRow(rowIndex)
        for (i in diceList.indices) {
            val dice = diceList[i]
            val diceImageView = getDiceImageResId(dice.value)
            val diceRowBinding = playerOneDiceRowBindings[rowIndex]
            val points = gameController.getRowPoints(rowIndex, 0)
            diceRowBinding.totalPoints.text = String.format("%d", points)

            when (i) {
                0 -> diceRowBinding.diceImage1.setImageResource(diceImageView)
                1 -> diceRowBinding.diceImage2.setImageResource(diceImageView)
                2 -> diceRowBinding.diceImage3.setImageResource(diceImageView)
            }

        }
    }
    private fun updateDiceRowPlayer2(rowIndex: Int) {
        val player = gameController.getPlayers()[1]
        val diceList = player.getDiceRow(rowIndex)
        for (i in diceList.indices) {
            val dice = diceList[i]
            val diceImageView = getDiceImageResId(dice.value)
            val diceRowBinding = playerTwoDiceRowBindings[rowIndex]
            val points = gameController.getRowPoints(rowIndex, 1)
            diceRowBinding.totalPoints.text = String.format("%d", points)

            when (i) {
                0 -> diceRowBinding.diceImage1.setImageResource(diceImageView)
                1 -> diceRowBinding.diceImage2.setImageResource(diceImageView)
                2 -> diceRowBinding.diceImage3.setImageResource(diceImageView)
            }

        }
    }


    // Function to get the correct drawable resource based on the dice value
    private fun getDiceImageResId(diceValue: Int): Int {
        return when (diceValue) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Exit")
        builder.setMessage("Are you sure you want to go back to the main screen?")
            .setPositiveButton("Yes") { _, _ -> returnToMainActivity() }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .show()

    }
    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    private fun showGameEndDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Game Over")
        when(val winner = gameController.getWinner()){
            0,1 -> dialogBuilder.setMessage("${gameController.getPlayers()[winner].name} wins!")
            else -> dialogBuilder.setMessage("It's a draw!")

        }

        dialogBuilder.setPositiveButton("New Round") { dialog, _ ->
            gameController.restart()
            updateDiceBoard()
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("End Game") { _, _ ->
            finish()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}




package com.example.knucklebones

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random





class GameActivity : AppCompatActivity() {

    private lateinit var rolledDiceImage: ImageView
    private lateinit var rollDiceButton: Button
    private val diceImages = listOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        rolledDiceImage = findViewById(R.id.rolled_dice_image)
        rollDiceButton = findViewById(R.id.roll_dice_button)

        // Set click listener for the Roll Dice button
        rollDiceButton.setOnClickListener {
            rollDiceWithAnimation()
        }




        // Example: 3 rows with 3 dice for Player 1
        val player1DiceRows = listOf(
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0))
        )

        // Example: 3 rows with 3 dice for Player 2
        val player2DiceRows = listOf(
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0)),
            listOf(Dice(0), Dice(0), Dice(0))
        )

        // Set up Player 1 dice rows
        setupDiceRow(
            rowId = R.id.dice_row_player_1_1,
            dice = player1DiceRows[0]
        )
        setupDiceRow(
            rowId = R.id.dice_row_player_1_2,
            dice = player1DiceRows[1]
        )
        setupDiceRow(
            rowId = R.id.dice_row_player_1_3,
            dice = player1DiceRows[2]
        )

        // Set up Player 2 dice rows
        setupDiceRow(
            rowId = R.id.dice_row_player_2_1,
            dice = player2DiceRows[0]
        )
        setupDiceRow(
            rowId = R.id.dice_row_player_2_2,
            dice = player2DiceRows[1]
        )
        setupDiceRow(
            rowId = R.id.dice_row_player_2_3,
            dice = player2DiceRows[2]
        )
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
                    val finalDiceValue = Random.nextInt(1, 7)
                    rolledDiceImage.setImageResource(getDiceImageResId(finalDiceValue))
                }
            }
        }
        handler.post(runnable)
    }

    // Function to set up a dice row (3 dice)
    private fun setupDiceRow(rowId: Int, dice: List<Dice>) {
        val diceRow = findViewById<android.widget.GridLayout>(rowId)

        // Update dice images
        val diceImages = listOf(
            diceRow.findViewById<ImageView>(R.id.dice_image_1),
            diceRow.findViewById<ImageView>(R.id.dice_image_2),
            diceRow.findViewById<ImageView>(R.id.dice_image_3)
        )

        // Loop through dice and set their images
        dice.forEachIndexed { index, dice ->
            val imageResId = getDiceImageResId(dice.value)
            diceImages[index].setImageResource(imageResId)
        }

        // Update the total points (sum of dice values)
        val totalPoints = dice.sumOf { it.value }
        val totalPointsTextView = diceRow.findViewById<TextView>(R.id.total_points)
        totalPointsTextView.text = totalPoints.toString()
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
}



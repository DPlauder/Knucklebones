package com.example.knucklebones

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import com.example.knucklebones.databinding.ActivityGameBinding
import com.example.knucklebones.databinding.DiceRowPlayer1Binding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameController: GameController
    private lateinit var dice_roll_section: LinearLayout
    private lateinit var rolledDiceImage: ImageView
    private lateinit var playerOneDiceRowBindings: List<DiceRowPlayer1Binding>
    private val diceImages = listOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        gameController = GameController()
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerOneDiceRowBindings = listOf(
            binding.diceRowPlayer11,
            binding.diceRowPlayer12,
            binding.diceRowPlayer13
        )


        rolledDiceImage = binding.rolledDiceImage

        dice_roll_section = binding.diceRollSection
        dice_roll_section.setOnClickListener{
            rollDiceWithAnimation()
            println("hello dice Roll")
        }
        setupDiceRowClickListener(playerOneDiceRowBindings)
    }

    private fun setupDiceRowClickListener(rows: List<DiceRowPlayer1Binding>) {
        println("hello row")
        for (i in rows.indices) {
            println(rows[i])
            val rowBinding = rows[i]
            rowBinding.root.setOnClickListener {
                println("hello row handler")
                println(rowBinding)

                if (gameController.validateRow(i)) {
                    gameController.setDiceInRow(i)
                }
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




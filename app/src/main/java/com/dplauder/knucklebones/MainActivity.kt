package com.dplauder.knucklebones

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton

import com.dplauder.knucklebones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Adjust padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDifficultySpinner()

        // Start GameActivity
        val startGameButton: Button = binding.startGameButton // Replace with your button ID
        startGameButton.setOnClickListener {

            val player1Name = getPlayerName(binding, R.id.player1_name_input, "Player 1")
            val player2Name = getPlayerName(binding, R.id.player2_name_input, "Player 2")
            val playerTypePlayerTwo = when (binding.player2TypeGroup.checkedRadioButtonId) {
                R.id.player2_human -> "Human"
                R.id.player2_cpu -> "CPU"
                else -> "Test"
            }

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("PLAYER_1_NAME", player1Name)
            intent.putExtra("PLAYER_2_NAME", player2Name)
            intent.putExtra("PLAYER_2_TYPE", playerTypePlayerTwo)

            if (playerTypePlayerTwo == "CPU") {
                val difficulty = getDifficulty(binding).toString()
                intent.putExtra("DIFFICULTY", difficulty)
            }

            startActivity(intent)
        }
        val rulesBtn: Button = binding.rulesButton
        rulesBtn.setOnClickListener {
            val rulesDialog = RulesDialogFragment()
            rulesDialog.show(supportFragmentManager, "RulesDialog")
        }


        val player2TypeGroup = binding.player2TypeGroup
        val player2NameInput = binding.player2NameInput
        val cpuDifficultyLayout = binding.cpuDifficultyLayout

        player2TypeGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.player2_human -> {
                    player2NameInput.visibility = View.VISIBLE
                    cpuDifficultyLayout.visibility = View.GONE
                }
                R.id.player2_cpu -> {
                    player2NameInput.visibility = View.GONE
                    cpuDifficultyLayout.visibility = View.VISIBLE
                }
            }
        }

    }
    private fun getPlayerName(binding: ActivityMainBinding, viewId: Int, defaultName: String): String {
        val editText = when(viewId) {
            R.id.player1_name_input -> binding.player1NameInput
            R.id.player2_name_input -> binding.player2NameInput
            else -> null
        }

        if (editText == null) return defaultName

        val playerName = editText.text.toString().trim()
        return playerName.ifEmpty { defaultName }
    }
    private fun setupDifficultySpinner() {
        val difficultyValues = KI.Difficulty.entries.map { it.name }.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.difficultySpinner.adapter = adapter
    }

    private fun getDifficulty(binding: ActivityMainBinding): KI.Difficulty {
        val difficultySpinner = binding.difficultySpinner
        val selectedDifficulty = difficultySpinner.selectedItem.toString()
        return KI.Difficulty.valueOf(selectedDifficulty)
    }
}
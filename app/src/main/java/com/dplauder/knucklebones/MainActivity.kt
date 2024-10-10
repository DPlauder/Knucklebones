package com.dplauder.knucklebones

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import com.dplauder.knucklebones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var playerNameOne: String
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

        // Start GameActivity when a button is clicked
        val startGameButton: Button = findViewById(R.id.start_game_button) // Replace with your button ID
        startGameButton.setOnClickListener {

            val player1Name = getPlayerName(R.id.player1_name_input, "Player 1")
            //val player2Name = getPlayerName(R.id.player2_name_input, "Player 2")

            val intent = Intent(this, GameActivity::class.java)

            intent.putExtra("PLAYER_1_NAME", player1Name)
            //intent.putExtra("PLAYER_2_NAME", player2Name)
            startActivity(intent)
        }
        val infoButton: ImageButton = binding.infoButton
        infoButton.setOnClickListener {
            // Show the rules dialog
            val rulesDialog = RulesDialogFragment()
            rulesDialog.show(supportFragmentManager, "RulesDialog")
        }
    }
    fun getPlayerName(viewId: Int, defaultName: String): String {
        val editText = findViewById<EditText>(viewId)
        val playerName = editText.text.toString().trim()
        return if (playerName.isEmpty()) defaultName else playerName
    }
}
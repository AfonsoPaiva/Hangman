package com.example.jogodaforca

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var playerNameEditText: EditText
    private lateinit var createRoomButton: Button
    private lateinit var joinRoomButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Authenticate anonymously if not already authenticated
        if (auth.currentUser == null) {
            authenticateAnonymously()
        }

        playerNameEditText = findViewById(R.id.playerNameEditText)
        createRoomButton = findViewById(R.id.createRoomButton)
        joinRoomButton = findViewById(R.id.joinRoomButton)

        // Load saved player name
        val sharedPreferences = getSharedPreferences("PlayerPrefs", Context.MODE_PRIVATE)
        val savedPlayerName = sharedPreferences.getString("playerName", "")
        playerNameEditText.setText(savedPlayerName)

        createRoomButton.setOnClickListener {
            val playerName = playerNameEditText.text.toString()
            if (playerName.isNotEmpty()) {
                savePlayerName(playerName)
                val intent = Intent(this, CreateRoomActivity::class.java)
                intent.putExtra("playerName", playerName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        joinRoomButton.setOnClickListener {
            val playerName = playerNameEditText.text.toString()
            if (playerName.isNotEmpty()) {
                savePlayerName(playerName)
                val intent = Intent(this, JoinRoomActivity::class.java)
                intent.putExtra("playerName", playerName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Authentication successful
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun savePlayerName(playerName: String) {
        val sharedPreferences = getSharedPreferences("PlayerPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("playerName", playerName)
        editor.apply()
    }
}
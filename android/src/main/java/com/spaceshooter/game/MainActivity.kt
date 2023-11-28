package com.spaceshooter.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.spaceshooter.game.android.AndroidLauncher
import MediaManager


class MainActivity : AppCompatActivity() {

    private lateinit var btnGame: Button
    private lateinit var btnHighscore: Button
    private lateinit var btnSettings: Button

    private lateinit var mediaPlayer: MediaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGame = findViewById(R.id.btn_game)
        btnHighscore = findViewById(R.id.btn_highscore)
        btnSettings = findViewById(R.id.btn_settings)

        mediaPlayer = MediaManager
        // Assuming you have a file named "test_music.mp3" in the res/raw directory
        val resourceId = resources.getIdentifier("test_music", "raw", packageName)
        mediaPlayer.initializeMediaPlayer(this, resourceId)
        mediaPlayer.startMediaPlayer()

        btnGame.setOnClickListener {
            // Start the game activity
            val intent = Intent(this, AndroidLauncher::class.java)
            //we want different music in gameplay window, we need to check how to implement that
            //finish()
            startActivity(intent)
        }
        btnHighscore.setOnClickListener {
            val intent = Intent(this, HighscoreActivity::class.java)
            startActivity(intent)
        }
        btnSettings.setOnClickListener{
            //Start the settings activity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        mediaPlayer.releaseMediaPlayer()
        finish()
    }

}

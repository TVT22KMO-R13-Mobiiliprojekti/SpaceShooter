package com.spaceshooter.game

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.spaceshooter.game.android.AndroidLauncher

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnGame: Button
    private lateinit var btnHighscore: Button
    private lateinit var btnSettings: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGame = findViewById(R.id.btn_game)
        btnHighscore = findViewById(R.id.btn_highscore)
        btnSettings = findViewById(R.id.btn_settings)

        // Assuming you have a file named "test_music.mp3" in the res/raw directory
        val resourceId = resources.getIdentifier("test_music", "raw", packageName)
        mediaPlayer = MediaPlayer.create(this, resourceId)
        mediaPlayer.start()

        btnGame.setOnClickListener {
            // Start the game activity
            val intent = Intent(this, AndroidLauncher::class.java)
            mediaPlayer.stop()
            finish()
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        mediaPlayer.release()
        finish()
    }

}

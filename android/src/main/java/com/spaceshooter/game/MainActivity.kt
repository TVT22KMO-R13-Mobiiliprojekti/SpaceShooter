package com.spaceshooter.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.spaceshooter.game.android.AndroidLauncher
import MediaManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var txtTitle : TextView

    private lateinit var btnGame: Button
    private lateinit var btnHighscore: Button
    private lateinit var btnSettings: Button

    private lateinit var mediaPlayer: MediaManager

    private val musicMenu = R.raw.menu_music


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtTitle = findViewById(R.id.title_text)
        //gradient colour for title
        val paint = txtTitle.paint
        val width = paint.measureText(txtTitle.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, 0f, txtTitle.textSize, intArrayOf(
            Color.parseColor("#EACF2A"),
            Color.parseColor("#EB972A"),
            Color.parseColor("#E00C13")
        ), null, Shader.TileMode.REPEAT)

        txtTitle.paint.setShader(textShader)

        btnGame = findViewById(R.id.btn_game)
        btnHighscore = findViewById(R.id.btn_highscore)
        btnSettings = findViewById(R.id.btn_settings)

        mediaPlayer = MediaManager
        // Assuming you have a file named "test_music.mp3" in the res/raw directory
        //val musicMenu = resources.getIdentifier("test_music", "raw", packageName)
        //val musicGame = resources.getIdentifier("test_music_2", "raw", packageName)

        //mediaPlayer.initializeMediaPlayer(this, musicMenu)
        mediaPlayer.playBackgroundSound(this, musicMenu)

        btnGame.setOnClickListener {
            // Start the game activity
            val intent = Intent(this, AndroidLauncher::class.java)
            //we want different music in gameplay window, we need to check how to implement that
            //mediaPlayer.stopMediaPlayer()
            //intent.putExtra("musicResource", musicGame)
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
    override fun onResume() {
        super.onResume()

        // Resume or start the original music when the MainActivity is resumed
        if (mediaPlayer.isMediaPlayerPlaying()) {
            //val musicMenu = resources.getIdentifier("test_music", "raw", packageName)
            //mediaPlayer.initializeMediaPlayer(this, musicMenu)
            //mediaPlayer.startMediaPlayer()
        }
    }
    override fun onBackPressed() {
        mediaPlayer.releaseMediaPlayer()
        finish()
    }

}

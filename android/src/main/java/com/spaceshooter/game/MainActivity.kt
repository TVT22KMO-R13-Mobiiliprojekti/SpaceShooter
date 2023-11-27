package com.spaceshooter.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.spaceshooter.game.android.AndroidLauncher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnGame = findViewById<Button>(R.id.btn_game)
        val btnHighscore = findViewById<Button>(R.id.btn_highscore)
        val btnSettings = findViewById<Button>(R.id.btn_settings)


        btnGame.setOnClickListener {
            val intent = Intent(this, AndroidLauncher::class.java)
            startActivity(intent)
        }
    }
}

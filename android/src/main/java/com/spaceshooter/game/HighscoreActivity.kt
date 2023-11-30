package com.spaceshooter.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HighscoreActivity : AppCompatActivity() {

    private lateinit var btnReturn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        btnReturn = findViewById(R.id.btn_back)

        btnReturn.setOnClickListener {
            finish()
        }
    }

}

package com.spaceshooter.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query

class EnterScoreActivity : AppCompatActivity(), ScoreHolder {

    private lateinit var hud: Hud

    private lateinit var btnReturn: Button

    private lateinit var txtTitle : TextView
    private lateinit var txtDescription : TextView
    private lateinit var editName : EditText

    //private var currentScore: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_score)

        btnReturn = findViewById(R.id.btn_back)
        txtTitle = findViewById(R.id.text_title)
        txtDescription = findViewById(R.id.text_desc)
        editName = findViewById(R.id.editText_name)

        val db = FirebaseFirestore.getInstance()
        //hud.setScoreHolder(this)


        // Create a query to get the first 10 high scores
        val query = db.collection("highscores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)

        //Log.d("EnterScoreActivity", "Current Score: $score")

        btnReturn.setOnClickListener {
            finish()
        }

    }

    override fun onScoreUpdated(score: Int) {
        Log.d("debug", "Updated Score: $score")
    }
}

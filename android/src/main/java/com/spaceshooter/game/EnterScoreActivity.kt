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

class EnterScoreActivity : AppCompatActivity() {

    private lateinit var btnReturn: Button
    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var editName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_score)

        btnReturn = findViewById(R.id.btn_back)
        txtTitle = findViewById(R.id.text_title)
        txtDescription = findViewById(R.id.text_desc)
        editName = findViewById(R.id.editText_name)



        val db = FirebaseFirestore.getInstance()
        //hud.setScoreHolder(this)

        // Get the value from the intent extra
        val score = intent.getIntExtra("score", 0) // default to 0 if not found

        val scoreText: String = "Your score was : $score"

        //txtDescription.text = scoreText
        // Log the received score for debugging
        Log.d("Received Score:", score.toString())


        // Create a query to get the first 10 high scores
        val query = db.collection("highscores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)

        query.get().addOnSuccessListener { querySnapshot ->
            val topScores = mutableListOf<Int>()

            // Iterate through the query results and populate the topScores list
            for (document in querySnapshot) {
                val scoreValue = document.getLong("score")
                if (scoreValue != null) {
                    topScores.add(scoreValue.toInt())
                }
            }

            // Check if the user's score is higher than the lowest top score
            if (topScores.isEmpty() || score > topScores.last()) {
                // User's score is among the top 10, update the UI accordingly
                txtTitle.text = "Congratulations!"
                txtDescription.text = scoreText
                editName.hint = "Enter your name"
                btnReturn.text = "Enter name"
                btnReturn.setOnClickListener {
                    val nickName = editName.text.toString()
                    insertNewHighScore(nickName,score)
                    finish()
                }

            } else {
                // User's score is not in the top 10, update the UI accordingly
                txtTitle.text = "Try Again!"
                txtDescription.text = "Sorry, but your score wasn't high enough."
                editName.isEnabled = false // Disable the EditText since the score isn't in the top 10
                btnReturn.text = "Back to menu"
            }
        }.addOnFailureListener { exception ->
            Log.e("EnterScoreActivity", "Error getting high scores", exception)
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }
    private fun insertNewHighScore(name: String, score: Int) {
        val db = FirebaseFirestore.getInstance()

        // Create a new high score entry with the user's name and score
        val newHighScore = hashMapOf(
            "nickname" to name,
            "score" to score
        )

        // Add the new high score to the "highscores" collection
        db.collection("highscores")
            .add(newHighScore)
            .addOnSuccessListener { documentReference ->
                Log.d("EnterScoreActivity", "Lisätty tietokantaan.")
            }
            .addOnFailureListener { e ->
                Log.e("EnterScoreActivity", "Virhe tietokantaan lisäämisessä", e)
            }
    }

}

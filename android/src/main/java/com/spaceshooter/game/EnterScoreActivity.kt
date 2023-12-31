package com.spaceshooter.game

import android.content.Intent
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

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_score)

        btnReturn = findViewById(R.id.btn_back)
        txtTitle = findViewById(R.id.text_title)
        txtDescription = findViewById(R.id.text_desc)
        editName = findViewById(R.id.editText_name)

        //hud.setScoreHolder(this)

        // Get the value from the intent extra
        val score = intent.getIntExtra("score", 0) // default to 0 if not found
        val scoreText: String = "Well done! You played well enough to earn your place in top 10. Your score was : $score"
        txtTitle.text = "GAME OVER"
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

                txtDescription.text = scoreText
                editName.hint = "Enter your name here."
                btnReturn.text = "Enter name"
                btnReturn.setOnClickListener {
                    if(editName.text.isEmpty())
                    {
                        editName.setError("Please enter your name.")
                        return@setOnClickListener
                    }
                    val nickName = editName.text.toString()
                    insertNewHighScore(nickName, score)
                    val intent = Intent(this, HighscoreActivity::class.java)
                    finish()
                    startActivity(intent)
                }

            } else {
                // User's score is not in the top 10, update the UI accordingly
                txtTitle.text = "Try Again!"
                txtDescription.text = "Sorry, but your score wasn't high enough."
                editName.isEnabled =
                    false // Disable the EditText since the score isn't in the top 10
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
                Log.d("EnterScoreActivity", "Lisäys tietokantaan onnistui")

                // After adding the new score, check if the number of scores exceeds a limit
                val limit : Long = 10 // Set your desired limit for the number of high scores, data type seems to be "Long" instead of "Int" in firestore database

                db.collection("highscores")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(limit + 1) // Get the top 10 + 1 scores
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val scoresToDelete = mutableListOf<String>()

                        // Iterate through the query results and populate the scoresToDelete list
                        for (document in querySnapshot) {
                            scoresToDelete.add(document.id)
                        }

                        // Delete the lowest score if there are more than the limit
                        if (scoresToDelete.size > limit) {
                            // Delete the lowest score (last in the list)
                            db.collection("highscores")
                                .document(scoresToDelete.last())
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("EnterScoreActivity", "Alin piste poistettu")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("EnterScoreActivity", "Virhe alimman pisteen poistamisessa tietokannasta", e)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EnterScoreActivity", "Virhe tietokannan lukemisessa", exception)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("EnterScoreActivity", "Virhe tietokantaan lisäyksessä", e)
            }
    }
}

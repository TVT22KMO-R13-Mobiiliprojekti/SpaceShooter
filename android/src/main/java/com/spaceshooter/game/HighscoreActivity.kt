package com.spaceshooter.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query

class HighscoreActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private lateinit var btnReturn: Button

    private lateinit var hsScreen: RecyclerView

    private lateinit var highScoreAdapter: HighScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        btnReturn = findViewById(R.id.btn_back)
        hsScreen = findViewById(R.id.HS_view)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Create a query to get the top 10 high scores
        val query = db.collection("highscores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)

        // Set up RecyclerView and attach the adapter
        hsScreen.layoutManager = LinearLayoutManager(this)
        val options = FirestoreRecyclerOptions.Builder<ScoreData>()
            .setQuery(query, ScoreData::class.java)
            .build()

        highScoreAdapter = HighScoreAdapter(options) // This is where you were missing the 'options'
        hsScreen.adapter = highScoreAdapter

        btnReturn.setOnClickListener {
            finish()
        }
    }
    // Start listening to the adapter when the activity starts
    override fun onStart() {
        super.onStart()
        highScoreAdapter.startListening()
    }

    // Stop listening to the adapter when the activity stops
    override fun onStop() {
        super.onStop()
        highScoreAdapter.stopListening()
    }
}

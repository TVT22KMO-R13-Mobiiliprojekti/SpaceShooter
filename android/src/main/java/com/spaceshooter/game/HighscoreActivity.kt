package com.spaceshooter.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query

class HighscoreActivity : AppCompatActivity() {

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

        // Create a query to get the first 10 high scores
        val query = db.collection("highscores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)

        // Set up RecyclerView and attach the adapter
        hsScreen.layoutManager = GridLayoutManager(this,2)

        // Create options for the first 10 scores
        val options = FirestoreRecyclerOptions.Builder<ScoreData>()
            .setQuery(query, ScoreData::class.java)
            .build()

        // Create and set the adapter
        highScoreAdapter = HighScoreAdapter(options)
        hsScreen.adapter = highScoreAdapter

        btnReturn.setOnClickListener {
            finish()
        }
    }

    // Start and stop listening to the adapter when the activity starts and stops
    override fun onStart() {
        super.onStart()
        highScoreAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        highScoreAdapter.stopListening()
    }
}

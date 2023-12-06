package com.spaceshooter.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HighScoreAdapter(options: FirestoreRecyclerOptions<ScoreData>) :
    FirestoreRecyclerAdapter<ScoreData, HighScoreAdapter.ScoreViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_highscore, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int, model: ScoreData) {
        holder.bind(model, position)
    }

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNickname: TextView = itemView.findViewById(R.id.textViewNickname)
        val textViewScore: TextView = itemView.findViewById(R.id.textViewScore)

        fun bind(scoreData: ScoreData, position: Int) {
            textViewNickname.text = scoreData.nickname
            textViewScore.text = scoreData.score.toString()
        }
    }
}


package com.example.themagicrun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themagicrun.R
import com.example.themagicrun.interfaces.Callback_HighScoreItemClicked
import com.example.themagicrun.model.HighScore
import com.google.android.material.textview.MaterialTextView

class HighScoreAdapter(
    private val callback: Callback_HighScoreItemClicked
) : RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {

    //list of records
    private var highScores: List<HighScore> = listOf()

    inner class HighScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //the components of each row in the table
        private val tvPosition: MaterialTextView = view.findViewById(R.id.score_LBL_position)
        private val tvName: MaterialTextView = view.findViewById(R.id.score_item_LBL_name)
        private val tvScore: MaterialTextView = view.findViewById(R.id.score_item_LBL_score)

        fun bind(highScore: HighScore, position: Int) {
            //updates the data in the row
            tvPosition.text = (position + 1).toString() //position starts from 0
            tvName.text = highScore.playerName
            tvScore.text = highScore.score.toString()

            itemView.setOnClickListener {
                callback.highScoreItemClicked(highScore.latitude, highScore.longitude)
            }
        }
    }

    //creates a new row in the table
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.high_score_item, parent, false)
        return HighScoreViewHolder(view)
    }

    //fills the data in a row
    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        holder.bind(highScores[position], position)
    }

    override fun getItemCount() = highScores.size

    fun updateScores(newScores: List<HighScore>) {
        highScores = newScores
        notifyDataSetChanged()
    }
}
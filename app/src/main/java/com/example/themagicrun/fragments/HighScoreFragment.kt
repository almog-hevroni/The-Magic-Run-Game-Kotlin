package com.example.themagicrun.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themagicrun.R
import com.example.themagicrun.Utilities.DataManager
import com.example.themagicrun.adapters.HighScoreAdapter
import com.example.themagicrun.interfaces.Callback_HighScoreItemClicked
import com.example.themagicrun.model.HighScore

class HighScoreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView //list of records
    private lateinit var adapter: HighScoreAdapter
    var highScoreItemClicked: Callback_HighScoreItemClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_high_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        loadHighScores()
    }

    private fun initViews(view: View) {
        adapter = HighScoreAdapter(object : Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                highScoreItemClicked?.highScoreItemClicked(lat, lon)
            }
        })

        recyclerView = view.findViewById(R.id.highScore_LST_scores)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
    }

    private fun loadHighScores() {
        val scores = DataManager.getScores()
        updateScores(scores)
    }

    fun updateScores(scores: List<HighScore>) {
        if (::adapter.isInitialized) {
            adapter.updateScores(scores)
        }
    }
}
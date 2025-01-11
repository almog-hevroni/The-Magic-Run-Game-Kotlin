package com.example.themagicrun

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.themagicrun.Utilities.DataManager
import com.example.themagicrun.fragments.HighScoreFragment
import com.example.themagicrun.fragments.MapFragment
import com.example.themagicrun.interfaces.Callback_HighScoreItemClicked
import com.google.android.material.button.MaterialButton


class RecordChartActivity : AppCompatActivity() {

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var backButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_chart)

        findViews()
        initViews()
        initFragments()
        loadHighScores()
    }

    private fun findViews() {
        backButton = findViewById(R.id.record_chart_BTN_back)
    }

    private fun initViews() {
        backButton.setOnClickListener {
            // Start MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            // Close current activity
            finish()
        }
    }

    private fun initFragments() {
        mapFragment = MapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.record_chart_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = HighScoreFragment()
        highScoreFragment.highScoreItemClicked = object : Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.zoom(lat, lon)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.record_chart_FRAME_scores, highScoreFragment)
            .commit()
    }

    private fun loadHighScores() {
        val scores = DataManager.getScores()
        highScoreFragment.updateScores(scores)
    }
}
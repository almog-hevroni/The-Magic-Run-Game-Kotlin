package com.example.themagicrun

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.themagicrun.Utilities.Constants
import com.example.themagicrun.Utilities.DataManager
import com.example.themagicrun.model.HighScore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textview.MaterialTextView
import android.Manifest
import android.util.Log
import com.example.themagicrun.Utilities.LocationUtils

class GameOverActivity : AppCompatActivity() {

    private lateinit var game_over_LBL_status: MaterialTextView
    private lateinit var game_over_LBL_score: MaterialTextView
    private lateinit var game_over_LBL_player_name: MaterialTextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViews()

        val bundle = intent.extras
        val message = bundle?.getString(Constants.BundleKeys.STATUS_KEY, "Magic Failed!")
        val finalScore = bundle?.getInt("SCORE", 0) ?: 0
        val playerName = bundle?.getString("PLAYER_NAME", "") ?: ""

        game_over_LBL_status.text = message
        game_over_LBL_score.text = String.format("Score: %s", finalScore.toString())
        game_over_LBL_player_name.text = String.format("Player: %s", playerName)

        Handler(Looper.getMainLooper()).postDelayed({
            saveScoreWithLocation(finalScore, playerName)
        }, 3000)

    }

    private fun findViews() {
        game_over_LBL_status = findViewById(R.id.game_over_LBL_status)
        game_over_LBL_score = findViewById(R.id.game_over_LBL_score)
        game_over_LBL_player_name = findViewById(R.id.game_over_LBL_player_name)
    }

    private fun saveScoreWithLocation(score: Int, playerName: String) {
        if (LocationUtils.checkLocationPermissions(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                saveScoreWithoutLocation(score, playerName)
                return
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val highScore = HighScore(
                        playerName = playerName,
                        score = score,
                        latitude = location?.latitude ?: 0.0,
                        longitude = location?.longitude ?: 0.0
                    )
                    DataManager.saveScore(highScore)
                    startLeaderboardActivity()
                }
                .addOnFailureListener {
                    saveScoreWithoutLocation(score, playerName)
                }
        } else {
            saveScoreWithoutLocation(score, playerName)
        }
    }

    private fun saveScoreWithoutLocation(score: Int, playerName: String) {
        val highScore = HighScore(
            playerName = playerName,
            score = score,
            latitude = 0.0,
            longitude = 0.0
        )
        DataManager.saveScore(highScore)
        startLeaderboardActivity()
    }

    private fun startLeaderboardActivity() {
        val intent = Intent(this, RecordChartActivity::class.java)
        startActivity(intent)
        finish()
    }


}
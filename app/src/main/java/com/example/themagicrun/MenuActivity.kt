package com.example.themagicrun

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.themagicrun.Utilities.Constants
import com.example.themagicrun.Utilities.GameMode
import com.example.themagicrun.Utilities.GameSpeed
import com.example.themagicrun.Utilities.SignalManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.Manifest
import com.example.themagicrun.Utilities.LocationUtils

class MenuActivity : AppCompatActivity() {
    private lateinit var buttons_play: MaterialButton
    private lateinit var sensors_play: MaterialButton
    private lateinit var start_game_button: MaterialButton
    private lateinit var name_input: TextInputEditText
    private var selectedGameMode: GameMode? = null
    private var selectedGameSpeed: GameSpeed? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_menu)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViews()
        initViews()

        //checking location permissions on first run
        if (!LocationUtils.checkLocationPermissions(this)) {
            LocationUtils.requestLocationPermissions(this)
        }
    }

    private fun findViews() {
        buttons_play = findViewById(R.id.buttons_play)
        sensors_play = findViewById(R.id.sensors_play)
        start_game_button = findViewById(R.id.start_game_button)
        name_input = findViewById(R.id.name_input)
    }

    private fun initViews() {
        //game with buttons
        buttons_play.setOnClickListener {
            selectedGameMode = GameMode.BUTTONS
            buttons_play.setBackgroundColor(getColor(R.color.selected_button))
            sensors_play.setBackgroundColor(getColor(R.color.unselected_button))
        }
        //game with sensors
        sensors_play.setOnClickListener {
            selectedGameMode = GameMode.SENSORS
            sensors_play.setBackgroundColor(getColor(R.color.selected_button))
            buttons_play.setBackgroundColor(getColor(R.color.unselected_button))
        }
        //easy game
        findViewById<MaterialButton>(R.id.easy_button).setOnClickListener {
            selectedGameSpeed = GameSpeed.SLOW
            it.setBackgroundColor(getColor(R.color.selected_button))
            findViewById<MaterialButton>(R.id.hard_button).setBackgroundColor(getColor(R.color.unselected_button))
        }
        //hard game
        findViewById<MaterialButton>(R.id.hard_button).setOnClickListener {
            selectedGameSpeed = GameSpeed.FAST
            it.setBackgroundColor(getColor(R.color.selected_button))
            findViewById<MaterialButton>(R.id.easy_button).setBackgroundColor(getColor(R.color.unselected_button))
        }

        findViewById<MaterialButton>(R.id.record_chart_button).setOnClickListener {
            val intent = Intent(this, RecordChartActivity::class.java)
            startActivity(intent)
        }

        start_game_button.setOnClickListener {
            when {
                //check inserted player name
                name_input.text.toString().isEmpty() -> {
                    SignalManager.getInstance().toast("Please enter your name")
                }
                //check inserted game mode
                selectedGameMode == null -> {
                    SignalManager.getInstance().toast("Please select game type")
                }
                //check inserted game speed
                selectedGameSpeed == null -> {
                    SignalManager.getInstance().toast("Please select difficulty level")
                }
                else -> {
                    startGame(selectedGameMode!!)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationUtils.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                SignalManager.getInstance().toast("Location permission granted")
            } else {
                SignalManager.getInstance().toast("Location permission denied - Some features might be limited")
            }
        }
    }

    private fun startGame(gameMode: GameMode) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(Constants.BundleKeys.GAME_MODE_KEY, gameMode.name)
            putExtra(Constants.BundleKeys.GAME_SPEED_KEY, selectedGameSpeed!!.name)
            putExtra("PLAYER_NAME", name_input.text.toString())
        }
        startActivity(intent)
        finish()
    }
}
package com.example.themagicrun

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.themagicrun.Utilities.Constants
import com.google.android.material.textview.MaterialTextView

class GameOverActivity : AppCompatActivity(){

    private lateinit var game_over_LBL_status: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        findViews()
        initViews()

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)

    }

    private fun findViews() {
        game_over_LBL_status = findViewById(R.id.game_over_LBL_status)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras
        val message = bundle?.getString(Constants.BundleKeys.STATUS_KEY, "Magic Failed!")
        game_over_LBL_status.text = message
    }


}
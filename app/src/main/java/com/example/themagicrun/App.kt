package com.example.themagicrun

import android.app.Application
import com.example.themagicrun.Utilities.SharedPreferencesManager
import com.example.themagicrun.Utilities.SignalManager

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        SharedPreferencesManager.init(this)

    }
}
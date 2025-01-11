package com.example.themagicrun.Utilities

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.Manifest

object LocationUtils {
    const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    fun checkLocationPermissions(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION //exact location (GPS)
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(activity: androidx.appcompat.app.AppCompatActivity) {
        //dialog window for the user to request permissions
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}
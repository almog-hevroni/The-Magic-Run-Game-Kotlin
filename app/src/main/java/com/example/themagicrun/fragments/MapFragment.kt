package com.example.themagicrun.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.themagicrun.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var defaultLocation = LatLng(31.0461, 34.8516) //Israel by default

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()
    }

    private fun setupMap() {
        googleMap?.apply {
            //basic map settings
            uiSettings.apply {
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isScrollGesturesEnabled = true
                isMapToolbarEnabled = true
            }

            //setting a default location
            moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 8f))

            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    fun zoom(lat: Double, lon: Double) {
        googleMap?.let { map ->
            val location = LatLng(lat, lon)

            //remove previous marker if present
            currentMarker?.remove()

            //add a new marker
            currentMarker = map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("High Score Location")
            )

            //moving the map to the new location with animation
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(location, 15f),
                1000,
                null
            )
        }
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}
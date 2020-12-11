package com.sanjaydevtech.mysurutourism

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.sanjaydevtech.mysurutourism.databinding.ActivityMapsBinding
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity() {
    private val binding: ActivityMapsBinding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }
    private var isClicked = false
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.maps_fragment_container) as SupportMapFragment
        val latDouble = intent.getDoubleExtra("lat", 0.0)
        val longDouble = intent.getDoubleExtra("long", 0.0)
        val placeTitle = intent.getStringExtra("title") ?: run { finish(); return }
        Log.d("MapsActivity", "Lat: $latDouble, Long: $longDouble")
        val latLong = LatLng(latDouble, longDouble)
        supportMapFragment.getMapAsync {
            it.uiSettings.apply {
                isZoomControlsEnabled = true
                isMapToolbarEnabled = true
                isIndoorLevelPickerEnabled = true
            }
            val markerOptions = MarkerOptions().position(latLong).title(placeTitle)
            it.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12.0f))
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        binding.optionsFab.setOnClickListener {
            if (!isClicked) {
                binding.centerFab.visibility = View.VISIBLE
                binding.routeFab.visibility = View.VISIBLE
                binding.centerFab.startAnimation(fromBottom)
                binding.routeFab.startAnimation(fromBottom)
                binding.optionsFab.startAnimation(rotateOpen)
            } else {
                binding.centerFab.visibility = View.GONE
                binding.routeFab.visibility = View.GONE
                binding.centerFab.startAnimation(toBottom)
                binding.routeFab.startAnimation(toBottom)
                binding.optionsFab.startAnimation(rotateClose)
            }
            isClicked = !isClicked
        }
        binding.centerFab.setOnClickListener {
            lifecycleScope.launch {
                supportMapFragment.awaitMap()
                    .animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12.0f))
            }
        }
        binding.routeFab.setOnClickListener {
            client.checkLocationSettings(builder.build())
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(this@MapsActivity, 777)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                }
        }

    }

    // TODO https://developer.android.com/training/location/request-updates#kotlin
}
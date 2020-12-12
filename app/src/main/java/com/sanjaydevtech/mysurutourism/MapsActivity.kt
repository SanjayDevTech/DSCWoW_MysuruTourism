package com.sanjaydevtech.mysurutourism

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.sanjaydevtech.mysurutourism.databinding.ActivityMapsBinding
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity() {
    private val binding: ActivityMapsBinding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private var requestingLocationUpdates = false
    private lateinit var currentLocation: Location
    private lateinit var locationCallback: LocationCallback
    private var isClicked = false
    private var currMarker: Marker? = null
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.maps_fragment_container) as SupportMapFragment
        val latDouble = intent.getDoubleExtra("lat", 0.0)
        val longDouble = intent.getDoubleExtra("long", 0.0)
        val placeTitle = intent.getStringExtra("title") ?: run { finish(); return }
        supportActionBar?.title = placeTitle
        //Log.d("MapsActivity", "Lat: $latDouble, Long: $longDouble")
        val latLong = LatLng(latDouble, longDouble)
        supportMapFragment.getMapAsync {
            it.uiSettings.apply {
                isZoomControlsEnabled = true
                isMapToolbarEnabled = true
                isIndoorLevelPickerEnabled = true
            }
            val markerOptions = MarkerOptions().position(latLong).title(placeTitle)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            (ContextCompat.getDrawable(
                                this,
                                R.drawable.tourist_place
                            ) as BitmapDrawable).bitmap, 80, 80, false
                        )
                    )
                )
            it.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12.0f))
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    requestingLocationUpdates = false
                    currentLocation = location
                    Log.d("MapsActivity", "onLocationRetrieved")
                    stopLocationUpdates()
                    lifecycleScope.launch {
                        val currLatLng = LatLng(location.latitude, location.longitude)

                        supportMapFragment.awaitMap().apply {
                            currMarker?.remove()
                            currMarker = addMarker(
                                MarkerOptions().position(currLatLng).title("Your Location")
                            )
                            animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 12.0f))
                        }

                    }
                }
            }
        }
        val builder = LocationSettingsRequest.Builder()
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
                    Log.d("MapsActivity", "In onSuccessListener")
                    if (it.locationSettingsStates.isGpsUsable) {
                        requestingLocationUpdates = true
                        startLocationUpdates()
                    } else {
                        requestingLocationUpdates = false
                        Toast.makeText(this, "Location is disabled", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(this@MapsActivity, 777)
                            Log.d("MapsActivity", "In onFailureListener: Try")
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                            Log.d("MapsActivity", "In onFailureListener: catch")
                        }
                    }
                }
        }

    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
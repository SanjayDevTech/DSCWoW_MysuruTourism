package com.sanjaydevtech.mysurutourism

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.databinding.ActivityPlaceBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceActivity : AppCompatActivity() {
    private val binding: ActivityPlaceBinding by lazy {
        ActivityPlaceBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MysuruApplication).repository)
    }
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "No title"
        val placeId = intent.getStringExtra("place_id") ?: run { finish(); return }
        mainViewModel.repository.placeDao().getPlaceById(placeId).observe(this@PlaceActivity) {
            if (it == null) {
                finish()
                return@observe
            }
            place = it
            supportActionBar?.let { actionBar ->
                actionBar.title = place.title
            } ?: run { Toast.makeText(this, "No subtitle", Toast.LENGTH_SHORT).show() }
            lifecycleScope.launch {
                Glide.with(this@PlaceActivity)
                    .load(place.img)
                    .dontTransform()
                    .placeholder(R.drawable.mysuru_festival_pink)
                    .into(binding.placeImg)
                binding.checkbox.isChecked = place.isBookmarked
            }
        }

        binding.checkboxAnimation.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    mainViewModel.repository.placeDao()
                        .updateBookmark(place.id, !place.isBookmarked)
                }
                binding.checkboxAnimation.likeAnimation()
            }
        }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    val mapsIntent = Intent(this, MapsActivity::class.java)
                    mapsIntent.putExtra("lat", place.lat)
                    mapsIntent.putExtra("long", place.lng)
                    mapsIntent.putExtra("title", place.title)
                    startActivity(mapsIntent)
                } else {

                    Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()

                }
            }
        binding.mapsBtn.setOnClickListener {
            when (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    val mapsIntent = Intent(this, MapsActivity::class.java)
                    mapsIntent.putExtra("lat", place.lat)
                    mapsIntent.putExtra("long", place.lng)
                    mapsIntent.putExtra("title", place.title)
                    startActivity(mapsIntent)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.place_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_maps -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
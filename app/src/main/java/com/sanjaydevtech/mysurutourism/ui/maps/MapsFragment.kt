package com.sanjaydevtech.mysurutourism.ui.maps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.sanjaydevtech.mysurutourism.PlaceActivity
import com.sanjaydevtech.mysurutourism.R
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.databinding.FragmentMapsBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val binding: FragmentMapsBinding by lazy { FragmentMapsBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment_container) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this) ?: run {
            Toast.makeText(requireContext(), "Error loading maps", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMarkerClickListener {
            val placeId: String = it.tag as String
            Snackbar.make(binding.root, "Would you like to open?", Snackbar.LENGTH_SHORT)
                .setAction("Open") {
                    val intent = Intent(requireContext(), PlaceActivity::class.java)
                    intent.putExtra("place_id", placeId)
                    startActivity(intent)
                }.show()
            it.showInfoWindow()
            true
        }
        lifecycleScope.launch {
            val bookmarkedPlaces: List<Place>
            withContext(Dispatchers.IO) {
                bookmarkedPlaces = mainViewModel.repository.placeDao().getPlacesByBookmarked()
            }
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(12.2958, 76.6394), 10.0f)
            )
            for (place in bookmarkedPlaces) {
                val latLong = LatLng(place.lat, place.lng)
                val markerOptions = MarkerOptions().position(latLong).title(place.title)
                map.addMarker(markerOptions).tag = place.id
            }
        }
    }
}
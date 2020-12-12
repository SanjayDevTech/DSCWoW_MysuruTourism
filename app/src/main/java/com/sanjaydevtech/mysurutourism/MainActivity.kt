package com.sanjaydevtech.mysurutourism

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sanjaydevtech.mysurutourism.databinding.ActivityMainBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MysuruApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id)?.findNavController()
                ?: run {
                    throw IllegalArgumentException("No NavController found")
                }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_maps
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        lifecycleScope.launch {
            val placesWithBookmarks = mainViewModel.repository.placeDao().getPlacesByBookmarked()
            mainViewModel.fetchPlacesFromRemote(true, placesWithBookmarks)
        }
    }
}
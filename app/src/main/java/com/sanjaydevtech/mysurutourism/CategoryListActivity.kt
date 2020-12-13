package com.sanjaydevtech.mysurutourism

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sanjaydevtech.mysurutourism.adapter.PlaceAdapter
import com.sanjaydevtech.mysurutourism.databinding.ActivityCategoryListBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModelFactory

class CategoryListActivity : AppCompatActivity() {
    private val binding: ActivityCategoryListBinding by lazy {
        ActivityCategoryListBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MysuruApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val categoryId = intent.getStringExtra("category_id") ?: run { finish(); return }
        val categoryTitle = intent.getStringExtra("category_title") ?: run { finish(); return }
        val categoryImg = intent.getIntExtra("category_img", R.drawable.temple)
        binding.appBarImage.setImageResource(categoryImg)
        binding.collapsingToolbar.title = categoryTitle
        val adapter = PlaceAdapter(this as AppCompatActivity)
        binding.placesListRv.adapter = adapter
        mainViewModel.repository.placeDao().getPlaceByCategory(categoryId).observe(this) {
            adapter.places = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
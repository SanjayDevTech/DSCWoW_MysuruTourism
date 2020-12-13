package com.sanjaydevtech.mysurutourism.data

import com.sanjaydevtech.mysurutourism.R

object DataSource {
    fun getCategories() = listOf(
        Category("historical", "Historical", R.drawable.historical),
        Category("temple", "Devotional", R.drawable.temple),
        Category("park", "Parks", R.drawable.park)
    )
}
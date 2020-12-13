package com.sanjaydevtech.mysurutourism.data

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var desc: String = "",
    var img: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var location: String = "",
    var category: String = "other",
    @ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean = false,

    @ColumnInfo(name = "featured")
    var isFeatured: Boolean = false,
)

data class Category(
    val id: String = "other",
    val title: String = "Other",
    @DrawableRes val imgRes: Int,
)
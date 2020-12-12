package com.sanjaydevtech.mysurutourism.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanjaydevtech.mysurutourism.data.Place

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE id=:id")
    fun getPlaceById(id: String): LiveData<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM place")
    suspend fun clear()

    @Query("SELECT * FROM place WHERE featured=:isFeatured LIMIT 10")
    fun getPlacesByFeatured(isFeatured: Boolean = true): LiveData<List<Place>>

    @Query("UPDATE place SET bookmarked=:isBookmarked WHERE id=:id")
    suspend fun updateBookmark(id: String, isBookmarked: Boolean)
}
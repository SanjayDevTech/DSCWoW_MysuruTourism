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
    suspend fun getPlaceById(id: String): Place?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM place")
    suspend fun clear()

    @Query("SELECT * FROM place WHERE featured=:isFeatured LIMIT 10")
    fun getPlacesByFeatured(isFeatured: Boolean = true): LiveData<List<Place>>
}
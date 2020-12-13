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

    @Query("SELECT * FROM place LIMIT :limit")
    suspend fun getLimitedPlaces(limit: Int = 10): List<Place>

    @Query("SELECT * FROM place WHERE id=:id")
    fun getPlaceById(id: String): LiveData<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM place")
    suspend fun clear()

    @Query("SELECT * FROM place WHERE category=:category")
    fun getPlaceByCategory(category: String = "other"): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE title LIKE :search OR location LIKE :search")
    suspend fun searchPlaceByTitle(search: String): List<Place>

    @Query("SELECT * FROM place WHERE featured=:isFeatured LIMIT :limit")
    fun getPlacesByFeatured(isFeatured: Boolean = true, limit: Int = 10): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE bookmarked=:isBookmarked")
    suspend fun getPlacesByBookmarked(isBookmarked: Boolean = true): List<Place>

    @Query("UPDATE place SET bookmarked=:isBookmarked WHERE id=:id")
    suspend fun updateBookmark(id: String, isBookmarked: Boolean)

    @Query("UPDATE place SET bookmarked=:isBookmarked WHERE id IN (:ids)")
    suspend fun updateBookmarkForIds(ids: Array<String>, isBookmarked: Boolean = true)
}
package com.sanjaydevtech.mysurutourism.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sanjaydevtech.mysurutourism.data.Place

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getInstance(applicationContext: Application): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext,
                    MainDatabase::class.java,
                    "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
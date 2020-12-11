package com.sanjaydevtech.mysurutourism.repository

import com.sanjaydevtech.mysurutourism.database.MainDatabase

class Repository(private val database: MainDatabase) {

    fun placeDao() = database.placeDao()
}
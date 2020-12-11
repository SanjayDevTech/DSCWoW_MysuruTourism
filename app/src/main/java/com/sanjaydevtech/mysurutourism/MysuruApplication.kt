package com.sanjaydevtech.mysurutourism

import android.app.Application
import com.sanjaydevtech.mysurutourism.database.MainDatabase
import com.sanjaydevtech.mysurutourism.repository.Repository

class MysuruApplication : Application() {
    private val database: MainDatabase by lazy {
        MainDatabase.getInstance(this)
    }
    val repository: Repository by lazy {
        Repository(database)
    }
}
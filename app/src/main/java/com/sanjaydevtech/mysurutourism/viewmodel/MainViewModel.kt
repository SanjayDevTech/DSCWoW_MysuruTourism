package com.sanjaydevtech.mysurutourism.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository) : ViewModel() {

    private val db = Firebase.firestore
    private val TAG = MainViewModel::class.simpleName

    suspend fun fetchPlacesFromRemote(
        block: Boolean = false,
        placesWithBookmarks: List<Place> = listOf()
    ) {
        db.collection("places").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val places: MutableList<Place> = mutableListOf()
                    val documents = task.result?.documents ?: return@addOnCompleteListener
                    for (document in documents) {
                        document.toObject(Place::class.java)?.let { it1 -> places.add(it1) }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.placeDao().apply {
                            clear()
                            insertAll(places)
                            if (block) {
                                repository.placeDao()
                                    .updateBookmarkForIds(placesWithBookmarks.map { it.id }
                                        .toTypedArray())
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Error: ${task.exception}")
                }
            }
    }

}
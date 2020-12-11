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

    suspend fun fetchPlacesFromRemote() {
        db.collection("places").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val places: MutableList<Place> = mutableListOf()
                    val documents = it.result?.documents ?: return@addOnCompleteListener
                    for (document in documents) {
                        document.toObject(Place::class.java)?.let { it1 -> places.add(it1) }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.placeDao().apply {
                            clear()
                            insertAll(places)
                        }
                    }
                } else {
                    Log.d(TAG, "Error: ${it.exception}")
                }
            }
    }

}
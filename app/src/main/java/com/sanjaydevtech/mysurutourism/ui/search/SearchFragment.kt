package com.sanjaydevtech.mysurutourism.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sanjaydevtech.mysurutourism.adapter.PlaceAdapter
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.databinding.FragmentSearchBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(
            layoutInflater
        )
    }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = PlaceAdapter(requireActivity() as AppCompatActivity)
        binding.placesRv.adapter = adapter
        lifecycleScope.launch {
            val placesList: List<Place>
            withContext(Dispatchers.IO) {
                placesList = mainViewModel.repository.placeDao().getLimitedPlaces(10)
            }
            adapter.places = placesList
            if (placesList.isEmpty()) {
                binding.placesRv.visibility = View.GONE
                binding.noResultsContainer.visibility = View.VISIBLE
            } else {
                binding.placesRv.visibility = View.VISIBLE
                binding.noResultsContainer.visibility = View.GONE
            }
        }
        binding.searchTextField.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val searchString = "%${binding.searchTextField.text.toString()}%"
                hideKeyboard()
                lifecycleScope.launch {
                    val placesList: List<Place>
                    withContext(Dispatchers.IO) {
                        placesList =
                            mainViewModel.repository.placeDao().searchPlaceByTitle(searchString)
                    }
                    adapter.places = placesList
                    if (placesList.isEmpty()) {
                        binding.placesRv.visibility = View.GONE
                        binding.noResultsContainer.visibility = View.VISIBLE
                    } else {
                        binding.placesRv.visibility = View.VISIBLE
                        binding.noResultsContainer.visibility = View.GONE
                    }
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.searchTextField.addTextChangedListener {
            val searchText = it.toString().trim()
            if (searchText.isEmpty()) {
                lifecycleScope.launch {
                    val placesList: List<Place>
                    withContext(Dispatchers.IO) {
                        placesList = mainViewModel.repository.placeDao().getLimitedPlaces(10)
                    }
                    adapter.places = placesList
                    if (placesList.isEmpty()) {
                        binding.placesRv.visibility = View.GONE
                        binding.noResultsContainer.visibility = View.VISIBLE
                    } else {
                        binding.placesRv.visibility = View.VISIBLE
                        binding.noResultsContainer.visibility = View.GONE
                    }
                }
            }
        }
        return binding.root
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
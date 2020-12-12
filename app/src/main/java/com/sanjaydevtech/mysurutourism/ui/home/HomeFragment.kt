package com.sanjaydevtech.mysurutourism.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sanjaydevtech.mysurutourism.adapter.PlaceAdapter
import com.sanjaydevtech.mysurutourism.databinding.FragmentHomeBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = PlaceAdapter(requireActivity() as AppCompatActivity)
        binding.placeListRv.adapter = adapter
        mainViewModel.repository.placeDao().getPlacesByFeatured().observe(viewLifecycleOwner) {
            adapter.places = it
            if (it.isEmpty()) {
                binding.placeListRv.visibility = View.GONE
                binding.noResultsContainer.visibility = View.VISIBLE
            } else {
                binding.placeListRv.visibility = View.VISIBLE
                binding.noResultsContainer.visibility = View.GONE
            }
        }
        return binding.root
    }
}
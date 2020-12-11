package com.sanjaydevtech.mysurutourism.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sanjaydevtech.mysurutourism.databinding.FragmentMapsBinding
import com.sanjaydevtech.mysurutourism.viewmodel.MainViewModel

class MapsFragment : Fragment() {

    private val binding: FragmentMapsBinding by lazy { FragmentMapsBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
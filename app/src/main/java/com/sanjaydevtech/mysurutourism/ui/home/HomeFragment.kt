package com.sanjaydevtech.mysurutourism.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sanjaydevtech.mysurutourism.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
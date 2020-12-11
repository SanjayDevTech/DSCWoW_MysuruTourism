package com.sanjaydevtech.mysurutourism.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sanjaydevtech.mysurutourism.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}
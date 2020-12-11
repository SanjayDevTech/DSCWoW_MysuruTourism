package com.sanjaydevtech.mysurutourism.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sanjaydevtech.mysurutourism.R
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.databinding.LayoutPlaceViewBinding

class PlaceAdapter(private val context: Context) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    var places: List<Place> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val adapterLayout =
            LayoutPlaceViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return PlaceViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.binding.apply {
            placeLocationTitle.text = place.location
            placeTitleText.text = place.title
            Glide.with(context)
                .load(place.img)
                .placeholder(R.drawable.mysuru_festival_pink)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(placeImg)
            placeContainer.setOnClickListener { }
            checkbox.isChecked = place.isBookmarked
            checkboxAnimation.setOnClickListener {
                checkbox.isChecked = !place.isBookmarked
                checkboxAnimation.likeAnimation()
                place.isBookmarked = checkbox.isChecked
            }
        }
    }

    override fun getItemCount() = places.size

    class PlaceViewHolder(val binding: LayoutPlaceViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
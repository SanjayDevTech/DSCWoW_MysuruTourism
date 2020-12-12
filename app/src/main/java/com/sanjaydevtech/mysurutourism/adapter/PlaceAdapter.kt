package com.sanjaydevtech.mysurutourism.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sanjaydevtech.mysurutourism.MysuruApplication
import com.sanjaydevtech.mysurutourism.PlaceActivity
import com.sanjaydevtech.mysurutourism.data.Place
import com.sanjaydevtech.mysurutourism.databinding.LayoutPlaceViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceAdapter(private val context: AppCompatActivity) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    private val repository by lazy {
        (context.application as MysuruApplication).repository
    }

    private val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

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
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
        holder.binding.apply {
            placeLocationTitle.text = place.location
            placeTitleText.text = place.title
            Glide.with(context)
                .load(place.img)
                .dontTransform()
                .placeholder(shimmerDrawable)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(placeImg)
            placeContainer.setOnClickListener {
                val intent = Intent(context, PlaceActivity::class.java)
                intent.putExtra("place_id", place.id)
                context.startActivity(intent)
            }
            checkbox.isChecked = place.isBookmarked
            checkboxAnimation.setOnClickListener {
                context.lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        repository.placeDao().updateBookmark(place.id, !place.isBookmarked)
                    }
                    checkboxAnimation.likeAnimation()
                }
            }
        }
    }

    override fun getItemCount() = places.size

    class PlaceViewHolder(val binding: LayoutPlaceViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
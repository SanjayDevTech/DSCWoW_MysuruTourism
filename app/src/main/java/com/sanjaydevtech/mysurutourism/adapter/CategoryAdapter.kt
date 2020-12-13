package com.sanjaydevtech.mysurutourism.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sanjaydevtech.mysurutourism.CategoryListActivity
import com.sanjaydevtech.mysurutourism.data.Category
import com.sanjaydevtech.mysurutourism.databinding.LayoutCategoryViewBinding

class CategoryAdapter(private val context: AppCompatActivity) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var categories: List<Category> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class CategoryViewHolder(val binding: LayoutCategoryViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val adapterLayout =
            LayoutCategoryViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryTitle.text = category.title
        holder.binding.categoryImg.setImageResource(category.imgRes)
        holder.binding.categoryContainer.setOnClickListener {
            val intent = Intent(context, CategoryListActivity::class.java)
            intent.apply {
                putExtra("category_id", category.id)
                putExtra("category_title", category.title)
                putExtra("category_img", category.imgRes)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = categories.size
}
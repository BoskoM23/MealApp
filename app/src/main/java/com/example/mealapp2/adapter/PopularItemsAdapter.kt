package com.example.mealapp2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp2.adapter.PopularItemsAdapter.PopularMealViewHolder
import com.example.mealapp2.data.MealsByCategories
import com.example.mealapp2.databinding.PopularItemsBinding

class PopularItemsAdapter : RecyclerView.Adapter<PopularMealViewHolder>() {
    var onLongItemClick: ((MealsByCategories) -> Unit)? = null
    lateinit var onItemClick: ((MealsByCategories) -> Unit)
    private var mealsList = ArrayList<MealsByCategories>()
    fun setMeals(mealsList: ArrayList<MealsByCategories>? = null) {
        if (mealsList != null) {
            this.mealsList = mealsList
        }
        notifyDataSetChanged()
    }

    inner class PopularMealViewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMeal)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}
package com.example.mealapp2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealapp2.data.Meal
import com.example.mealapp2.databinding.MealItemBinding

class FavouriteMealsAdapter :
    RecyclerView.Adapter<FavouriteMealsAdapter.FavouriteMealsViewHolder>() {
    inner class FavouriteMealsViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {
        return FavouriteMealsViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldMeal: Meal, newMeal: Meal): Boolean =
            oldMeal.idMeal == newMeal.idMeal

        override fun areContentsTheSame(oldMeal: Meal, newMeal: Meal): Boolean = oldMeal == newMeal
    }
    val differ = AsyncListDiffer(this, diffUtil)
}
package com.example.mealapp2.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mealapp2.R
import com.example.mealapp2.adapter.CategoryMealsAdapter
import com.example.mealapp2.databinding.ActivityCategoryMealsBinding
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.fragments.HomeFragment
import com.example.mealapp2.view.CategoryMealsViewModel
import com.example.mealapp2.view.CategoryMealsViewModelFactory

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_category_meals)
        prepareRecyclerView()
        val mealDB = MealDB.getInstance(this)
        val categoryMealsViewModelFactory = CategoryMealsViewModelFactory(mealDB)
        categoryMealsViewModel = ViewModelProvider(this, categoryMealsViewModelFactory)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategories(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observeMealsByCategoriesLiveData()
            .observe(this) { mealsList ->
                if (mealsList != null) {
                    categoryMealsAdapter.setMealsList(mealsList)
                }
            }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.mealRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}
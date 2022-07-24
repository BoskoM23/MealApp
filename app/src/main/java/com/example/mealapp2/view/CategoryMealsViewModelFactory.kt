package com.example.mealapp2.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealapp2.db.MealDB

class CategoryMealsViewModelFactory(val mealDB: MealDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryMealsViewModel(mealDB) as T
    }
}
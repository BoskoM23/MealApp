package com.example.mealapp2.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mealapp2.data.MealsByCategories
import com.example.mealapp2.data.MealsByCategoriesList
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.netwoork.MealClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class CategoryMealsViewModel(private val mealDB: MealDB) : ViewModel() {
    val mealsByCategoriesLiveData = MutableLiveData<List<MealsByCategories>>()

    fun getMealsByCategories(categoryName: String) {
        MealClient.mealService.getCategoriesByMeal(categoryName).enqueue(object : Callback,
            retrofit2.Callback<MealsByCategoriesList> {

            override fun onResponse(
                call: Call<MealsByCategoriesList>,
                response: Response<MealsByCategoriesList>
            ) {
                response.body()?.let { mealsByCategoriesList ->
                    mealsByCategoriesLiveData.postValue(mealsByCategoriesList.catList)
                }
            }

            override fun onFailure(call: Call<MealsByCategoriesList>, t: Throwable) {
                Log.d("Category items", t.message.toString())
            }
        })
    }

    fun observeMealsByCategoriesLiveData(): LiveData<List<MealsByCategories>> {
        return mealsByCategoriesLiveData
    }
}
package com.example.mealapp2.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp2.data.Meal
import com.example.mealapp2.data.MealList
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.netwoork.MealClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MealViewModel(private val mealDB: MealDB) : ViewModel() {
    private val mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id: String) {
        MealClient.mealService.getMealDetails(id).enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Meal Activity", t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDB.mealDao().insert(meal)
        }
    }
}
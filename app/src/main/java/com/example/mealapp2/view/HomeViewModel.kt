package com.example.mealapp2.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp2.data.*
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.netwoork.MealClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeViewModel(private val mealDB: MealDB) : ViewModel() {
    private val mutableMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategories>>()
    private val categoriesLiveData = MutableLiveData<List<Category>>()
    private val favoritesLiveData = mealDB.mealDao().getAllMeals()
    private val mealsBottomSheetLiveData = MutableLiveData<Meal>()

    init {
        getRandomMeals()
    }
    private fun getRandomMeals() {
        MealClient.mealService.getRandomMeals().enqueue(object : Callback,
            retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    mutableMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home fragment", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        MealClient.mealService.getPopularItems("Seafood").enqueue(object : Callback,
            retrofit2.Callback<MealsByCategoriesList> {
            override fun onResponse(
                call: Call<MealsByCategoriesList>,
                response: Response<MealsByCategoriesList>
            ) {
                response.body() ?. let { mealsList ->
                    popularItemsLiveData.postValue((mealsList.catList))
                }
                /*if (response.body() != null) {
                    popularItemsLiveData.value = response.body()!!.catList
                } else {
                    return
                }*/
            }

            override fun onFailure(call: Call<MealsByCategoriesList>, t: Throwable) {
                Log.d("Popular items", t.message.toString())
            }
        })
    }

    fun getCategories(){
        MealClient.mealService.getCategories().enqueue(object : Callback,
        retrofit2.Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body() ?. let { categoriesList ->
                    categoriesLiveData.postValue((categoriesList.categories))
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Category items", t.message.toString())
            }
        })
    }

    fun getMealById(id: String){
        MealClient.mealService.getMealDetails(id).enqueue(object : Callback,
            retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { _meal ->
                    mealsBottomSheetLiveData.postValue(_meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Bottom sheet", t.message.toString())
            }
        })
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDB.mealDao().insert(meal)
        }
    }
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDB.mealDao().delete(meal)
        }
    }
    fun observeRandomMealLiveData(): LiveData<Meal> {
        return mutableMealLiveData
    }
    fun observeFavoritesMealLiveData(): LiveData<List<Meal>>{
        return favoritesLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategories>> {
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData() : LiveData<List<Category>>{
        return categoriesLiveData
    }
    fun observeBottomSheetLiveData() : LiveData<Meal> = mealsBottomSheetLiveData
}
package com.example.mealapp2.netwoork

import com.example.mealapp2.data.CategoryList
import com.example.mealapp2.data.MealsByCategoriesList
import com.example.mealapp2.data.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealService {
    @GET("random.php")
    fun getRandomMeals(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") idString: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoriesList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php?")
    fun getCategoriesByMeal(@Query("c") categoryName: String): Call<MealsByCategoriesList>
}
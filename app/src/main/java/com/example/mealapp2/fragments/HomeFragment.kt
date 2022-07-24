package com.example.mealapp2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mealapp2.activity.CategoryMealsActivity
import com.example.mealapp2.activity.MainActivity
import com.example.mealapp2.activity.MealActivity
import com.example.mealapp2.adapter.CategoriesAdapter
import com.example.mealapp2.adapter.PopularItemsAdapter
import com.example.mealapp2.data.Category
import com.example.mealapp2.data.Meal
import com.example.mealapp2.data.MealsByCategories
import com.example.mealapp2.databinding.FragmentHomeBinding
import com.example.mealapp2.view.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularItemsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.mealapp2.fragments.idMeal"
        const val MEAL_NAME = "com.example.mealapp2.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.mealapp2.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.mealapp2.fragments.strCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        binding = FragmentHomeBinding.inflate(layoutInflater)
        popularItemsAdapter = PopularItemsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparedPopularItemsRecyclerView()
        // viewModel.getRandomMeals()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()

        preparedCategoryRecyclerView()
        viewModel.getCategories()
        observerCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealsBottomSheetFragment = MealsBottomSheetFragment.newInstance(meal.idMeal)
            mealsBottomSheetFragment.show(childFragmentManager, "Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun preparedCategoryRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.categoryMealRv.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategories(categories as ArrayList<Category>)
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparedPopularItemsRecyclerView() {
        popularItemsAdapter = PopularItemsAdapter()
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        //ovde je greska, null can't be casted to non null type
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealsList ->
            popularItemsAdapter.setMeals(mealsList as? ArrayList<MealsByCategories>)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.randomMeal)
                this.randomMeal = meal
            }
    }

    private fun onRandomMealClick() {
        binding.randomMealCv.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
}
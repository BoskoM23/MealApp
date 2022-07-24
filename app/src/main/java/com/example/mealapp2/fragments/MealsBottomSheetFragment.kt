package com.example.mealapp2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mealapp2.activity.MainActivity
import com.example.mealapp2.activity.MealActivity
import com.example.mealapp2.databinding.FragmentMealsBottomSheetBinding
import com.example.mealapp2.fragments.HomeFragment.Companion.MEAL_ID
import com.example.mealapp2.view.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MealsBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealsBottomSheetBinding
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealById(it) }
        observeBottomSheetMeal()
        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgCategory)
            binding.tvMealCategory.text = meal.strCategory
            binding.tvMealCountry.text = meal.strArea
            binding.tvMealNameInBtmsheet.text = meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        }
    }

    companion object {
        fun newInstance(param1: String) = MealsBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString(MEAL_ID, param1)
            }
        }
    }
}
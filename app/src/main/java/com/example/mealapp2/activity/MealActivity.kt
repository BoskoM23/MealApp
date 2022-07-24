package com.example.mealapp2.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mealapp2.data.Meal
import com.example.mealapp2.databinding.ActivityMealBinding
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.fragments.HomeFragment
import com.example.mealapp2.view.MealViewModel
import com.example.mealapp2.view.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealID: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDB = MealDB.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDB)
        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]
        //mealViewModel = ViewModelProviders.of(this)[MealViewModel::class.java]
        getMealInfoFromIntent()
        setInfoInViews()
        mealViewModel.getMealDetails(mealID)
        observerMealDetailsLiveData()
        onYouTubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.floatButton.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYouTubeImageClick() {
        binding.imageYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(
            this
        ) { t ->
            mealToSave = t
            binding.tvCategories.text = "Category : ${t!!.strCategory}"
            binding.tvArea.text = "Area : ${t.strArea}"
            binding.tvInstructionsTips.text = t.strInstructions
            youtubeLink = t.strYoutube.toString()
        }
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageMealDetails)
        binding.collapsToolbar.title = mealName
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealID = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
}
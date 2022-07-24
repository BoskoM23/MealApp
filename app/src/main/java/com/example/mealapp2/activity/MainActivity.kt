package com.example.mealapp2.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mealapp2.R
import com.example.mealapp2.db.MealDB
import com.example.mealapp2.view.HomeViewModel
import com.example.mealapp2.view.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDB = MealDB.getInstance(this)
        val homeViewModelProviderFragment = HomeViewModelFactory(mealDB)
        ViewModelProvider(this, homeViewModelProviderFragment)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? NavHostFragment
        val navController = navHostFragment?.navController
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}
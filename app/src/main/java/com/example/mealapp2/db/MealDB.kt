package com.example.mealapp2.db

import android.content.Context
import androidx.room.*
import com.example.mealapp2.data.Meal

@Database(entities = [Meal::class], version = 6)
@TypeConverters(MealTypeConverter::class)
abstract class MealDB : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var mealDB: MealDB? = null

        @Synchronized
        fun getInstance(context: Context): MealDB {
            if (mealDB == null) {
                mealDB = Room.databaseBuilder(
                    context,
                    MealDB::class.java,
                    "meal_db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return mealDB as MealDB
        }
    }
}
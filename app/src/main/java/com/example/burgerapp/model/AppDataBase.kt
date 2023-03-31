package com.example.burgerapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Food::class, User::class , Cart::class, Type::class), version = 1 )

abstract class AppDataBase: RoomDatabase() {

    abstract fun getFoodDao(): FoodDao
    abstract fun getUserDao(): UsersDao
    abstract fun getCartDao(): CartDao
    abstract fun getTypeDao(): TypeDao


    companion object {
        fun getAppDataBase(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context!!.applicationContext,
                AppDataBase::class.java,
                "app_database"
            ).build()

        }
    }
}

package com.example.burgerapp.model

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.GET

@Dao
interface FoodDao {
    @Insert
    fun insertFood(food: Food)

    @Delete
    fun deleteFood(food: Food)

    @Update
    fun updateFood(food: Food)

    @Query("Select * from foodTable where name = :name")
    fun getFoodByName(name: String): Food

    @Query("Select * from foodTable")
    fun getAllFood(): LiveData<List<Food>>

    @Query("Select * from foodTable where food_type = :type")
    fun getFoodByType(type: Etype): LiveData<List<Food>>

    @Query("Select * from foodTable limit :pageSize offset :offset")
    fun getFoodListByPage(pageSize: Int, offset: Int): List<Food>


}
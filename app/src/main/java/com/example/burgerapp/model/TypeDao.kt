package com.example.burgerapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TypeDao {
    @Insert
    fun insertType(type: Type)

    @Delete
    fun deleteType(type: Type)

    @Query("Select * from typesTable")
    fun getAllTypes(): LiveData<List<Type>>

    @Query("Select * from typesTable where name = :name")
    fun getTypeByName(name: String): Type

}
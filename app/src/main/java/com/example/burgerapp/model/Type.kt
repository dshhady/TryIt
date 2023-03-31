package com.example.burgerapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



enum class  Etype{
    BURGER,PIZZA,SHAWARMA,FALAFEL,OTHER
}
@Entity(tableName = "typesTable")
data class Type(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image_res") var imageRes: String? = null,
    @ColumnInfo(name = "food_type") var foodType: Etype
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

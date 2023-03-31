package com.example.burgerapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



enum class ImageType{
    RES,GALLERY
}
@Entity(tableName = "foodTable")
data class Food(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price : Int,
    @ColumnInfo(name = "image_res") var imageRes: String? = null,
    @ColumnInfo(name = "food_type") var foodType: Etype,
    @ColumnInfo(name = "image_type") var imageType: ImageType,
    @ColumnInfo(name = "calories") var calories: Int
){

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}






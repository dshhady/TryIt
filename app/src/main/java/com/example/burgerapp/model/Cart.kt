package com.example.burgerapp.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cartsTable", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)
])
data class Cart(
                @ColumnInfo(name = "name") var name: String,
                @ColumnInfo(name = "price") var price : Int,
                @ColumnInfo(name = "image_res") var imageRes: String? = null,
                val user_id: Int,
                @ColumnInfo(name = "image_type") var imageType: ImageType? = null,
                @ColumnInfo(name = "quantity") var quantity: Int = 1
){
   @PrimaryKey(autoGenerate = true)
   var id: Int = 0
}







package com.example.burgerapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class USER_TYPE{
    ADMIN,USER
}

@Entity(tableName = "usersTable")
data class User(
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "image") var image: String? = null,
    @ColumnInfo(name = "user_type") var userType: USER_TYPE = USER_TYPE.USER
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


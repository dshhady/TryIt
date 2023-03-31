package com.example.burgerapp.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("?key=24600132-0ec495cba698daad9745fb31e&image_type=photo&pretty=true")
    fun getImages(@Query("q")name : String): Call<ApiResponseHitsList>
    companion object{
        const val BASE_URL = "https://pixabay.com/api/"
        fun create ():ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }

}
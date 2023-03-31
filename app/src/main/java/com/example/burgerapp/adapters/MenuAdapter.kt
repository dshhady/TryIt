package com.example.burgerapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.burgerapp.R
import com.example.burgerapp.model.Food
import com.example.burgerapp.model.ImageType

class MenuAdapter (
    private val context: Context,
    private val foodList: List<Food>,
        ): RecyclerView.Adapter<MenuAdapter.ViewHolder>(){



    class ViewHolder (foodView: View) : RecyclerView.ViewHolder(foodView) {

        val imageView: ImageView
        val name_textView : TextView
        val price_textView : TextView
        val calories_textView : TextView


        init {
            imageView = foodView.findViewById(R.id.menu_item_image)
            name_textView = foodView.findViewById(R.id.menu_item_name)
            price_textView = foodView.findViewById(R.id.menu_item_price)
            calories_textView = foodView.findViewById(R.id.menu_item_calories)


        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.name_textView.text = foodItem.name
        holder.price_textView.text = foodItem.price.toString()+"₪"
        holder.calories_textView.text = foodItem.calories.toString()+"kcal"

        if (foodItem.imageRes != null)
            if (foodItem.imageType == ImageType.RES)
                Glide.with(context).load(foodItem.imageRes).into(holder.imageView)
            else
                Glide.with(context).load(Uri.parse(foodItem.imageRes)).into(holder.imageView)

    }




}




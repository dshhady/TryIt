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
import com.example.burgerapp.model.Cart
import com.example.burgerapp.model.Food
import com.example.burgerapp.model.ImageType
import com.squareup.picasso.Picasso


class searchListAdapter(private val context: Context,
                        private val filteredList: List<Food>,
                        val addFoodSearchToCart:(Cart)->Unit,
                        private val userId : Int,
                        ):RecyclerView.Adapter<searchListAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val name_textView : TextView
        val price_textView : TextView
        val addButton : ImageView
        val calories_textView : TextView

        init {
            imageView = itemView.findViewById(R.id.foodImage_iv)
            name_textView = itemView.findViewById(R.id.foodName_tv)
            price_textView = itemView.findViewById(R.id.foodPrice_tv)
            addButton = itemView.findViewById(R.id.add_button)
            calories_textView = itemView.findViewById(R.id.tv_calories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.food_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = filteredList[position]
        holder.name_textView.text = foodItem.name
        holder.price_textView.text = foodItem.price.toString()+"₪"
        holder.calories_textView.text = foodItem.calories.toString()+"kcal"
        if (foodItem.imageRes != null)
            if (foodItem.imageType == ImageType.RES)
                Picasso.get().load(foodItem.imageRes).into(holder.imageView)
            else if (foodItem.imageType == ImageType.GALLERY)
                Glide.with(context).load(Uri.parse(foodItem.imageRes)).into(holder.imageView)


        holder.addButton.setOnClickListener{
            addFoodSearchToCart(Cart(foodItem.name, foodItem.price, foodItem.imageRes,userId, foodItem.imageType))
        }
    }






}
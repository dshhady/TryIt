package com.example.burgerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapp.R
import com.example.burgerapp.model.Type
import com.squareup.picasso.Picasso

class FoodTypesAdapter (
    private val typeList: List<Type>,
    private val onTypeClick: (Type) -> Unit
        ): RecyclerView.Adapter<FoodTypesAdapter.ViewHolder>() {


    class ViewHolder (typeView: View) : RecyclerView.ViewHolder(typeView){
        val textView : TextView
        val imageView: ImageView

        init {
            textView = itemView.findViewById(R.id.text_view)
            imageView = itemView.findViewById(R.id.image_view)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rv_type, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val typeItem = typeList[position]
        holder.textView.text = typeItem.name
        Picasso.get()
            .load(typeItem.imageRes)
            .into(holder.imageView)
        holder.itemView.setOnClickListener{
            onTypeClick(typeItem)
        }
    }




}

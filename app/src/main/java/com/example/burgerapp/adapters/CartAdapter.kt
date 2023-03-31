package com.example.burgerapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.burgerapp.R
import com.example.burgerapp.model.Cart
import com.example.burgerapp.model.ImageType
import com.squareup.picasso.Picasso


class CartAdapter (
    private val context: Context,
    private val cartList : MutableList<Cart>,
    private val removeFoodFromCart:(Cart)-> Unit,
    private val updateCartQuantity:(Cart)-> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>()
{
    class CartViewHolder (foodView: View) : RecyclerView.ViewHolder(foodView){

        val imageCart : ImageView
        val foodName_tv: TextView
        val foodPrice_tv : TextView
        val removeButton : ImageView
        val plusButton : ImageView
        val minusButton : ImageView
        val quantity_tv : TextView

        init {
            imageCart = foodView.findViewById(R.id.imageCart)
            foodName_tv = foodView.findViewById(R.id.foodName_tv)
            foodPrice_tv = foodView.findViewById(R.id.foodPrice_tv)
            removeButton = foodView.findViewById(R.id.remove_button)
            plusButton = foodView.findViewById(R.id.plus_button)
            minusButton = foodView.findViewById(R.id.minus_button)
            quantity_tv = foodView.findViewById(R.id.quantity_tv)

        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.foodName_tv.text = cartItem.name
        holder.quantity_tv.text = cartItem.quantity.toString()
        holder.foodPrice_tv.text = (cartItem.price * cartItem.quantity).toString()+"₪"
        if (cartItem.imageRes != null)
            if (cartItem.imageType == ImageType.RES)
                Picasso.get().load(cartItem.imageRes).into(holder.imageCart)
            else if (cartItem.imageType == ImageType.GALLERY)
                Glide.with(context).load(Uri.parse(cartItem.imageRes)).into(holder.imageCart)

        holder.removeButton.setOnClickListener { //remove food from cart
            onRemoveFoodFromCartClick(cartItem,position,holder)
        }
        holder.plusButton.setOnClickListener {  // increment quantity
            onFoodIncrementQuantityClick(cartItem,position,holder)
        }
        holder.minusButton.setOnClickListener { // decrement quantity
            onDecrementQuantityClick(cartItem,position)
        }
    }

    private fun onRemoveFoodFromCartClick(cartItem:Cart, position: Int, holder: CartViewHolder) {
        cartList.removeAt(position)
        Toast.makeText(context, "${holder.foodName_tv.text} Removed from cart", Toast.LENGTH_SHORT).show()
        removeFoodFromCart(cartItem)
        notifyDataSetChanged()
    }

    private fun onFoodIncrementQuantityClick(cartItem:Cart, position: Int, holder: CartViewHolder) {
        cartItem.quantity++
        updateCartQuantity(cartItem)
        LinearLayoutManager(context).scrollToPosition(position)
        notifyDataSetChanged()
    }

    private fun onDecrementQuantityClick(cartItem: Cart, position: Int) {
        if (cartItem.quantity > 1) {
            cartItem.quantity--
            updateCartQuantity(cartItem)
            notifyDataSetChanged()
            LinearLayoutManager(context).scrollToPosition(position)
        }

    }


}
package com.example.burgerapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.burgerapp.model.Repository
import com.example.burgerapp.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = Repository.getInstance(application.applicationContext)
    val cartData : LiveData<List<Cart>> = repository.getAllCart()

    fun addFoodToCart(cart : Cart){
        GlobalScope.launch (Dispatchers.IO){
        repository.addFoodToCart(cart)
        }
    }

    fun removeFoodFromCart(cart: Cart){
        repository.removeFoodFromCart(cart)
    }

    fun getCartByUserId(userId: Int): LiveData<List<Cart>> {
            return repository.getCartByUserId(userId)
    }

    fun updateCartQuantity(cart: Cart) {
        val cartId = cart.id
        val cartQuantity = cart.quantity
        repository.updateCartQuantity(cartId, cartQuantity)
    }

    fun deleteAllCartByUserId(userId: Int) {
        repository.deleteAllCartByUserId(userId)
    }


}
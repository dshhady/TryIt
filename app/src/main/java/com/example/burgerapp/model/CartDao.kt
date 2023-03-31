package com.example.burgerapp.model
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface  CartDao {
    @Insert
    fun insertCart(cart: Cart)

    @Delete
    fun deleteCart(cart: Cart)

    @Query("Select * from cartsTable")
    fun getAllCart(): LiveData<List<Cart>>

    @Query("Select * from cartsTable where user_id = :userId")
    fun getCartByUserId(userId: Int): LiveData<List<Cart>>

    @Query("Select * from cartsTable where id = :cartId")
     fun getCartById(cartId: Int): Cart

    @Query("Update cartsTable set quantity = :cartQuantity where id = :cartId")
    fun updateCartById(cartId: Int, cartQuantity: Int)

    @Query("Update cartsTable set quantity = :cartQuantity where id = :cartId")
    fun updateCartQuantity(cartId: Int, cartQuantity: Int)

    @Query("Delete from cartsTable where user_id = :userId")
    fun deleteAllCartByUserId(userId: Int)


}






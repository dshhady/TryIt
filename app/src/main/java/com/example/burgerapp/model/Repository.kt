package com.example.burgerapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource

class Repository private constructor(context: Context) {

    private val foodDao = AppDataBase.getAppDataBase(context).getFoodDao()
    private val usersDao = AppDataBase.getAppDataBase(context).getUserDao()
    private val cartDao = AppDataBase.getAppDataBase(context).getCartDao()
    private val typeDao = AppDataBase.getAppDataBase(context).getTypeDao()


    companion object {
        private lateinit var instance: Repository

        fun getInstance(context: Context): Repository {
            if (!Companion::instance.isInitialized) {
                instance = Repository(context)
            }
            return instance
        }
    }

    //-----------------Food Repository-----------------//

    fun addFood(food: Food) {
        foodDao.insertFood(food)
    }

    fun getAllFood(): LiveData<List<Food>> {
        return foodDao.getAllFood()
    }

    fun addFoodIfNotExist(food: Food) {
        if (foodDao.getFoodByName(food.name) == null) {
            foodDao.insertFood(food)
        }
    }


    //-----------------User Repository-----------------//

    fun getAllUsers(): LiveData<List<User>> {
        return usersDao.getAllUsers()
    }

    fun addUser(user: User) {
        usersDao.insertUser(user)
    }

    fun getUserById(userId: Int): User {
        return usersDao.getUserById(userId)
    }

    fun getUserByEmail(email: String): User {
        return usersDao.getUserByEmail(email)

    }

    fun updateUserImage(id: Int, toString: String) {
        usersDao.updateUserImage(id, toString)
    }

    fun updateUserName(userId: Int, userName: String) {
        usersDao.updateUserName(userId, userName)
    }

    fun updateUserEmail(userId: Int, email: String) {
        usersDao.updateUserEmail(userId, email)
    }

    fun updateUserPassword(userId: Int, password: String) {
        usersDao.updateUserPassword(userId, password)
    }

    //-----------------Cart Repository-----------------//

    fun addFoodToCart(cart: Cart) {
        cartDao.insertCart(cart)
    }

    fun getAllCart(): LiveData<List<Cart>> {
        return cartDao.getAllCart()
    }

    fun removeFoodFromCart(cart: Cart) {
        cartDao.deleteCart(cart)
    }

    fun getCartByUserId(userId: Int): LiveData<List<Cart>> {
        return cartDao.getCartByUserId(userId)
    }

    fun updateCartQuantity(cartId: Int, cartQuantity: Int) {
        cartDao.updateCartQuantity(cartId, cartQuantity)
    }

    fun deleteAllCartByUserId(userId: Int) {
        cartDao.deleteAllCartByUserId(userId)
    }

    //-----------------Types Repository-----------------//


    fun getAllTypes(): LiveData<List<Type>> {
        return typeDao.getAllTypes()
    }

    fun getFoodByType(type: Etype): LiveData<List<Food>> {
        return foodDao.getFoodByType(type)
    }

    fun addTypeIfNotExist(type: Type) {
        if (typeDao.getTypeByName(type.name) == null) {
            typeDao.insertType(type)
        }
    }



}
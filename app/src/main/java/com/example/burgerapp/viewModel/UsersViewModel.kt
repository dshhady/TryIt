package com.example.burgerapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.burgerapp.model.Repository
import com.example.burgerapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UsersViewModel(val app: Application): AndroidViewModel(app) {

    private val repository = Repository.getInstance(app.applicationContext)
    val usersData: LiveData<List<User>> = repository.getAllUsers()


    fun addUser(user: User) {
        repository.addUser(user)
    }

    fun getUserById(userId: Int): User {
        return repository.getUserById(userId)
    }

    fun updateUserImage(id: Int, toString: String) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.updateUserImage(id, toString)
        }
    }

    fun updateUserName(userId: Int, userName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.updateUserName(userId, userName)
        }
    }

    fun updateUserEmail(userId: Int, email: String) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.updateUserEmail(userId, email)
        }
    }

    fun updateUserPassword(userId: Int, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.updateUserPassword(userId, password)
        }
    }

    fun addUserIfNotExist(adminUser: User) {
        GlobalScope.launch(Dispatchers.IO) {
            if (repository.getUserByEmail(adminUser.email) == null) {
                repository.addUser(adminUser)
            }
        }
    }

}

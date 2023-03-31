package com.example.burgerapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.burgerapp.model.Etype
import com.example.burgerapp.model.Repository
import com.example.burgerapp.model.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TypeViewModel (val app: Application): AndroidViewModel(app) {

    private val repository = Repository.getInstance(app.applicationContext)
    val typesData: LiveData<List<Type>> = repository.getAllTypes()

    private fun addTypeIfNotExist(type: Type) {
        repository.addTypeIfNotExist(type)
    }

    fun prepareTypes() {
        GlobalScope.launch(Dispatchers.IO){
            addTypeIfNotExist(Type("Burger","https://i.ibb.co/h2b7jXW/menu-burger.jpg", Etype.BURGER))
            addTypeIfNotExist(Type("Pizza","https://i.ibb.co/pW7zr8z/menu-pizza.jpg", Etype.PIZZA))
            addTypeIfNotExist(Type("Shawarma","https://i.ibb.co/1TRWD80/menu-shawarma.jpg", Etype.SHAWARMA))
            addTypeIfNotExist(Type("Falafel","https://i.ibb.co/wBXXPLp/menu-falafel.jpg", Etype.FALAFEL))
        }

    }


}

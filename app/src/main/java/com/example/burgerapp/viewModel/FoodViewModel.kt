package com.example.burgerapp.viewModel
import android.app.Application
import androidx.lifecycle.*
import com.example.burgerapp.model.Etype
import com.example.burgerapp.model.Food
import com.example.burgerapp.model.ImageType
import com.example.burgerapp.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FoodViewModel (application: Application) : AndroidViewModel(application){
     private val repository = Repository.getInstance(application.applicationContext)
     var foodData : LiveData<List<Food>> = repository.getAllFood()

    fun addFood(food : Food){
        GlobalScope.launch (Dispatchers.IO){
            repository.addFood(food)
        }
    }
    private fun addFoodIfNotExist(food : Food){
        repository.addFoodIfNotExist(food)
    }
    fun getFoodByType(type: Etype): LiveData<List<Food>> {
        return repository.getFoodByType(type)
    }

    fun prepareFoodData(){
        GlobalScope.launch (Dispatchers.IO){
            addFoodIfNotExist(Food("Cheeseburger", 50,"https://i.ibb.co/vw2GgkZ/cheese-burger.jpg", Etype.BURGER, ImageType.RES, 535))
            addFoodIfNotExist(Food("Bacon Burger", 45, "https://i.ibb.co/R2NDpN4/baconburger.jpg",Etype.BURGER, ImageType.RES,595))
            addFoodIfNotExist(Food("Chicken Burger", 60, "https://i.ibb.co/vLqmvRG/chicken-burger.jpg",Etype.BURGER, ImageType.RES,535))
            addFoodIfNotExist(Food("Veggie Burger", 40, "https://i.ibb.co/85g8RVq/veggie-burger.jpg",Etype.BURGER, ImageType.RES,365))
            addFoodIfNotExist(Food("Beef Shawarma", 45, "https://i.ibb.co/TBpYtkY/beef-shawarma.jpg",Etype.SHAWARMA, ImageType.RES,673))
            addFoodIfNotExist(Food("Chicken Shawarma", 35, "https://i.ibb.co/3vnPFnv/chicken-shwarma.jpg",Etype.SHAWARMA, ImageType.RES,528))
            addFoodIfNotExist(Food("Shawarma Plate", 30, "https://i.ibb.co/G3gXH9D/shawarma-plate.jpg",Etype.SHAWARMA, ImageType.RES,492))
            addFoodIfNotExist(Food("Shawarma with Hummous", 40, "https://i.ibb.co/SBVPCTC/shawarma-with-hummuos.jpg",Etype.SHAWARMA, ImageType.RES,600))
            addFoodIfNotExist(Food("Cheese Pizza", 50, "https://i.ibb.co/kx39m2R/cheese-pizza.webp",Etype.PIZZA, ImageType.RES,452))
            addFoodIfNotExist(Food("Veggie Pizza", 55, "https://i.ibb.co/6stb4Xh/veggie-pizza.webp",Etype.PIZZA, ImageType.RES,350))
            addFoodIfNotExist(Food("Pepperoni Pizza", 60, "https://i.ibb.co/gb2tjZJ/pepperonu-pizza.webp",Etype.PIZZA, ImageType.RES,313))
            addFoodIfNotExist(Food("Meat Pizza", 45, "https://i.ibb.co/KKwrFtj/meat-pizza.webp",Etype.PIZZA, ImageType.RES,382))
            addFoodIfNotExist(Food("Margherita Pizza", 60, "https://i.ibb.co/rf56TRZ/margherita-pizza.webp",Etype.PIZZA, ImageType.RES,270))
            addFoodIfNotExist(Food("BBQ Chicken Pizza", 60, "https://i.ibb.co/BwGfv0x/bbq-chicken-pizza.webp",Etype.PIZZA, ImageType.RES,540))
            addFoodIfNotExist(Food("Hawaiian Pizza", 50, "https://i.ibb.co/ZSD6XZK/hawaiian-pizza.webp",Etype.PIZZA, ImageType.RES,314))
            addFoodIfNotExist(Food("The Works Pizza", 60, "https://i.ibb.co/QkjJz7G/the-worker-pizza.webp",Etype.PIZZA, ImageType.RES,501))
            addFoodIfNotExist(Food("Traditional Falafel", 15, "https://i.ibb.co/Vx6rgLn/traditional-falafel.webp",Etype.FALAFEL, ImageType.RES,150))
            addFoodIfNotExist(Food("Vegetable Falafel", 20, "https://i.ibb.co/zPGPtQH/vegetable-falafel.webp",Etype.FALAFEL, ImageType.RES,90))
            addFoodIfNotExist(Food("Egyptian Falafel", 15, "https://i.ibb.co/G9rQrk6/egyptian-falafel.webp",Etype.FALAFEL, ImageType.RES,203))
            addFoodIfNotExist(Food("Falafel with Hummous", 35, "https://i.ibb.co/7bfSVpq/falafel-with-hummuos.jpg",Etype.FALAFEL, ImageType.RES,472))
        }
    }





}
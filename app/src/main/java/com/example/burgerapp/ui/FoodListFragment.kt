package com.example.burgerapp.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.burgerapp.adapters.FoodAdapter
import com.example.burgerapp.R
import com.example.burgerapp.model.*
import com.example.burgerapp.viewModel.CartViewModel
import com.example.burgerapp.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_food_list.*

class FoodListFragment(private var userId : Int, private var foodType: Etype, private var ShowFullMenu : Boolean) : Fragment() {
    private val foodViewModel : FoodViewModel by viewModels()
    private val cartViewModel : CartViewModel by viewModels()
    private var maxPageCount = 4 // the max number of pages
    private val foods = mutableListOf<Food>() // list of the food to show in the recycler view

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }
    override fun onStart() {
        super.onStart()
        displayFullMenuOrOneType() // show the full menu or the food by type
    }

    //-------------menu Showing and recycler view creation-----------------
    private fun displayFullMenuOrOneType(){
        if (ShowFullMenu) { // if the user want to see all the food (showFullMenu = true)
            loadMore_fr.visibility = View.VISIBLE // show the load more button
            pagination() // show the first 4 items
            loadMore_fr.setOnClickListener {
                pagination() // show the next 4 items
            }
        }
        else{ // if the user want to see only the food by type (ShowFullMenu = false)
            loadMore_fr.visibility = View.GONE // hide the load more button
            foodViewModel.getFoodByType(foodType).observe(this){ it -> // get the food by type
                createFoodRecyclerView(it.toMutableList(),userId)
            }
        }

    }
    private fun createFoodRecyclerView(foodList: MutableList<Food>, userId: Int) {
        val layoutManager = recyclerViewDataFood_food_list_fr.layoutManager as LinearLayoutManager
        val adapter = FoodAdapter(requireActivity(), foodList, addFoodToCart(), userId!!)
        if (ShowFullMenu)
            layoutManager.scrollToPosition(adapter.itemCount - 1) // scroll to the last item
        recyclerViewDataFood_food_list_fr.adapter = adapter
    }

    //-----------------pagination-----------------//
    private fun pagination() {
        foodViewModel.foodData.observe(this) { it ->
            var foodsInPage = 1
            for (user in it.sortedBy { it.calories }) {
                if (!foods.contains(user))
                    foods.add(user)
                if (foodsInPage % maxPageCount == 0 || foodsInPage >= it.size) {
                    createFoodRecyclerView(foods,userId)
                    maxPageCount += maxPageCount
                    break
                }
                foodsInPage++
            }
        }
    }
    //-------------add food to cart (used in adapter)-----------------//
     private fun addFoodToCart() :(cart: Cart)  -> Unit = { cart->
        var flag = false
        cartViewModel.cartData.observe(this){
            flag = it.contains(cart) // check if the food is already in the cart
        }
        if(flag){ // if the food is already in the cart
            Toast.makeText(requireActivity(),"This food is already in your cart",Toast.LENGTH_LONG).show()
        }
        else { // if the food is not in the cart
            cartViewModel.addFoodToCart(cart) // add the food to the cart
            Toast.makeText(requireActivity(), "${cart.name} Added to cart", Toast.LENGTH_SHORT).show()
        }

    }

}